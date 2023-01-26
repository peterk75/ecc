import java.security.SecureRandom;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.generators.KDF2BytesGenerator;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.Security;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemObject;
import java.io.FileReader;
import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.security.PublicKey;
import java.math.BigInteger;
import org.bouncycastle.jce.spec.ECParameterSpec;
import static org.bouncycastle.jce.ECNamedCurveTable.*;


public class bc {

    public static String byteArrayToHex(byte[] a) {
        if (a == null) return "null";
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    public static void main(String[] args) {

        try {
            Security.addProvider(new BouncyCastleProvider());

            PemReader pemReader = new PemReader(new FileReader(new File("public.key")));

            PemObject pemObject = pemReader.readPemObject();
            System.out.println("P-KEY: " + Base64.getEncoder().encodeToString(pemObject.getContent()));
            X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(pemObject.getContent());
            KeyFactory factory = KeyFactory.getInstance("EC", "BC");
            PublicKey pubKey = factory.generatePublic(encodedKeySpec);
            KDF2BytesGenerator kdf = new KDF2BytesGenerator(new SHA1Digest());
            ECParameterSpec params = getParameterSpec("secp256r1");
            HMac hmac = new HMac(new SHA1Digest());
            int macKeylength = 10; //80 Bits;

            ECPoint Q = ECAlgorithms.cleanPoint(params.getCurve(), ((ECPublicKey) pubKey).getQ());
            if (Q.isInfinity()) {
                throw new IllegalStateException("Infinity is not a valid public key for ECDH");
            }


            System.out.println(pubKey);

            String cvv = "123";
            System.out.println("cvv: " + cvv);

            byte[] message = cvv.getBytes();
            System.out.println("Message hex encoded: " + byteArrayToHex(message));

            SecureRandom sec = new SecureRandom();
            sec.setSeed(12345L);

            byte[] values = new byte[32];
            sec.nextBytes(values);
            System.out.println("Random bytes: " + byteArrayToHex(values));
            BigInteger r = new BigInteger(values);
            System.out.println("Random r: " + r.toString());

            BigInteger n = params.getN();
            while (r.compareTo(n) == 1) {
                sec.nextBytes(values);
                System.out.println("Random bytes: " + byteArrayToHex(values));
                r = new BigInteger(values);
                System.out.println("Random r: " + r.toString());
            }
            ECPoint P = ECAlgorithms.referenceMultiply(Q, r).normalize();
            if (P.isInfinity()) {
                throw new IllegalStateException("Infinity is not a valid agreement value for ECDH");
            }

            BigInteger sharedS = P.getAffineXCoord().toBigInteger();
            System.out.println("SharedSecret: " + sharedS.toString());
	    
 	    byte[] sharedSb = null;
            if (sharedS.toByteArray()[0]==0)
	    {
	      sharedSb=new byte[sharedS.toByteArray().length-1];
	      System.arraycopy(sharedS.toByteArray(),1,sharedSb,0,sharedSb.length);
	    } else { sharedSb = sharedS.toByteArray(); }
	    System.out.println("SharedSecret: " + byteArrayToHex(sharedSb));
	


            //get R
            ECPoint G = params.getG();
            if (G.isInfinity()) {
                throw new IllegalStateException("Infinity is not a valid public key for ECDH");
            }
            ECPoint R = ECAlgorithms.referenceMultiply(G, r).normalize();
            if (R.isInfinity()) {
                throw new IllegalStateException("Infinity is not a valid agreement value for ECDH");
            }
            System.out.println("R: " + byteArrayToHex(R.getEncoded(false)));
            int Rlen=R.getEncoded(false).length;

            byte[] kdfb = new byte[message.length + macKeylength];
            KDFParameters kdfparam = new KDFParameters(sharedSb, null);
            kdf.init(kdfparam);
            kdf.generateBytes(kdfb, 0, message.length + macKeylength);
            System.out.println("KDF: " + byteArrayToHex(kdfb));


            byte[] encrypted = new byte[message.length];
            // perform XOR operation of key
            // with every character in message
            for (int i = 0; i < message.length; i++) {
                encrypted[i] = (byte) (message[i] ^ kdfb[i]);
            }
            System.out.println("encryptedMessage: " + byteArrayToHex(encrypted));



            hmac.init(new KeyParameter(kdfb,message.length,macKeylength));
            hmac.update(encrypted,0,encrypted.length);
            //SHA1 digest lent 160b = 20B
            byte[] mac= new byte[hmac.getMacSize()];
            hmac.doFinal(mac,0);

            System.out.println("macMessage: " + byteArrayToHex(mac));


            byte[] Response = new byte[Rlen+mac.length+ encrypted.length];
            System.arraycopy(R.getEncoded(false), 0, Response, 0, Rlen);
            System.arraycopy(encrypted, 0, Response, Rlen,encrypted.length);
            System.arraycopy(mac,0,Response,Rlen+ encrypted.length , mac.length);
            System.out.println("MessageResponse: " + byteArrayToHex(Response));

            File outputFile = new File("encrypted");
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                outputStream.write(Response);
            }


        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

}
