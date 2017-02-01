package com.spongycastle.openpgp.examples;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Date;

import com.spongycastle.bcpg.ArmoredOutputStream;
import com.spongycastle.bcpg.CompressionAlgorithmTags;
import com.spongycastle.jce.provider.BouncyCastleProvider;
import com.spongycastle.openpgp.PGPCompressedData;
import com.spongycastle.openpgp.PGPCompressedDataGenerator;
import com.spongycastle.openpgp.PGPEncryptedDataGenerator;
import com.spongycastle.openpgp.PGPEncryptedDataList;
import com.spongycastle.openpgp.PGPException;
import com.spongycastle.openpgp.PGPLiteralData;
import com.spongycastle.openpgp.PGPLiteralDataGenerator;
import com.spongycastle.openpgp.PGPPBEEncryptedData;
import com.spongycastle.openpgp.PGPUtil;
import com.spongycastle.openpgp.jcajce.JcaPGPObjectFactory;
import com.spongycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import com.spongycastle.openpgp.operator.jcajce.JcePBEDataDecryptorFactoryBuilder;
import com.spongycastle.openpgp.operator.jcajce.JcePBEKeyEncryptionMethodGenerator;
import com.spongycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import com.spongycastle.util.io.Streams;

/**
 * Simple routine to encrypt and decrypt using a passphrase.
 * This service routine provides the basic PGP services between
 * byte arrays.
 * 
 * Note: this code plays no attention to -CONSOLE in the file name
 * the specification of "_CONSOLE" in the filename.
 * It also expects that a single pass phrase will have been used.
 * 
 */
public class ByteArrayHandler
{
    /**
     * decrypt the passed in message stream
     * 
     * @param encrypted  The message to be decrypted.
     * @param passPhrase Pass phrase (key)
     * 
     * @return Clear text as a byte array.  I18N considerations are
     *         not handled by this routine
     * @exception IOException
     * @exception PGPException
     * @exception NoSuchProviderException
     */
    public static byte[] decrypt(
        byte[] encrypted,
        char[] passPhrase)
        throws IOException, PGPException, NoSuchProviderException
    {
        InputStream in = new ByteArrayInputStream(encrypted);

        in = PGPUtil.getDecoderStream(in);

        JcaPGPObjectFactory         pgpF = new JcaPGPObjectFactory(in);
        PGPEncryptedDataList     enc;
        Object                          o = pgpF.nextObject();
        
        //
        // the first object might be a PGP marker packet.
        //
        if (o instanceof PGPEncryptedDataList)
        {
            enc = (PGPEncryptedDataList)o;
        }
        else
        {
            enc = (PGPEncryptedDataList)pgpF.nextObject();
        }

        PGPPBEEncryptedData pbe = (PGPPBEEncryptedData)enc.get(0);

        InputStream clear = pbe.getDataStream(new JcePBEDataDecryptorFactoryBuilder(new JcaPGPDigestCalculatorProviderBuilder().setProvider("SC").build()).setProvider("SC").build(passPhrase));

        JcaPGPObjectFactory        pgpFact = new JcaPGPObjectFactory(clear);

        PGPCompressedData   cData = (PGPCompressedData)pgpFact.nextObject();

        pgpFact = new JcaPGPObjectFactory(cData.getDataStream());

        PGPLiteralData ld = (PGPLiteralData)pgpFact.nextObject();

        return Streams.readAll(ld.getInputStream());
    }

    /**
     * Simple PGP encryptor between byte[].
     * 
     * @param clearData  The test to be encrypted
     * @param passPhrase The pass phrase (key).  This method assumes that the
     *                   key is a simple pass phrase, and does not yet support
     *                   RSA or more sophisiticated keying.
     * @param fileName   File name. This is used in the Literal Data Packet (tag 11)
     *                   which is really inly important if the data is to be
     *                   related to a file to be recovered later.  Because this
     *                   routine does not know the source of the information, the
     *                   caller can set something here for file name use that
     *                   will be carried.  If this routine is being used to
     *                   encrypt SOAP MIME bodies, for example, use the file name from the
     *                   MIME type, if applicable. Or anything else appropriate.
     *                             
     * @param armor
     * 
     * @return encrypted data.
     * @exception IOException
     * @exception PGPException
     * @exception NoSuchProviderException
     */
    public static byte[] encrypt(
        byte[]  clearData,
        char[]  passPhrase,
        String  fileName,
        int     algorithm,
        boolean armor)
        throws IOException, PGPException, NoSuchProviderException
    {
        if (fileName == null)
        {
            fileName= PGPLiteralData.CONSOLE;
        }

        byte[] compressedData = compress(clearData, fileName, CompressionAlgorithmTags.ZIP);

        ByteArrayOutputStream bOut = new ByteArrayOutputStream();

        OutputStream out = bOut;
        if (armor)
        {
            out = new ArmoredOutputStream(out);
        }

        PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(new JcePGPDataEncryptorBuilder(algorithm).setSecureRandom(new SecureRandom()).setProvider("SC"));
        encGen.addMethod(new JcePBEKeyEncryptionMethodGenerator(passPhrase).setProvider("SC"));

        OutputStream encOut = encGen.open(out, compressedData.length);

        encOut.write(compressedData);
        encOut.close();

        if (armor)
        {
            out.close();
        }

        return bOut.toByteArray();
    }

    private static byte[] compress(byte[] clearData, String fileName, int algorithm) throws IOException
    {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        PGPCompressedDataGenerator comData = new PGPCompressedDataGenerator(algorithm);
        OutputStream cos = comData.open(bOut); // open it with the final destination

        PGPLiteralDataGenerator lData = new PGPLiteralDataGenerator();

        // we want to generate compressed data. This might be a user option later,
        // in which case we would pass in bOut.
        OutputStream  pOut = lData.open(cos, // the compressed output stream
                                        PGPLiteralData.BINARY,
                                        fileName,  // "filename" to store
                                        clearData.length, // length of clear data
                                        new Date()  // current time
                                      );

        pOut.write(clearData);
        pOut.close();

        comData.close();

        return bOut.toByteArray();
    }

    public static void main(String[] args) throws Exception
    {
        Security.addProvider(new BouncyCastleProvider());
        
        String passPhrase = "Dick Beck";
        char[] passArray = passPhrase.toCharArray();

        byte[] original = "Hello world".getBytes();
        System.out.println("Starting PGP test");
        byte[] encrypted = encrypt(original, passArray, "iway", PGPEncryptedDataGenerator.CAST5, true);

        System.out.println("\nencrypted data = '"+new String(encrypted)+"'");
        byte[] decrypted= decrypt(encrypted,passArray);

        System.out.println("\ndecrypted data = '"+new String(decrypted)+"'");
        
        encrypted = encrypt(original, passArray, "iway", PGPEncryptedDataGenerator.AES_256, false);

        System.out.println("\nencrypted data = '"+new String(com.spongycastle.util.encoders.Hex.encode(encrypted))+"'");
        decrypted= decrypt(encrypted, passArray);

        System.out.println("\ndecrypted data = '"+new String(decrypted)+"'");
    }
}
