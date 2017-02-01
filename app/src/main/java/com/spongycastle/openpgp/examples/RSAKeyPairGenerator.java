package com.spongycastle.openpgp.examples;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.SignatureException;
import java.util.Date;

import com.spongycastle.bcpg.ArmoredOutputStream;
import com.spongycastle.bcpg.HashAlgorithmTags;
import com.spongycastle.jce.provider.BouncyCastleProvider;
import com.spongycastle.openpgp.PGPEncryptedData;
import com.spongycastle.openpgp.PGPException;
import com.spongycastle.openpgp.PGPKeyPair;
import com.spongycastle.openpgp.PGPPublicKey;
import com.spongycastle.openpgp.PGPSecretKey;
import com.spongycastle.openpgp.PGPSignature;
import com.spongycastle.openpgp.operator.PGPDigestCalculator;
import com.spongycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import com.spongycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import com.spongycastle.openpgp.operator.jcajce.JcaPGPKeyPair;
import com.spongycastle.openpgp.operator.jcajce.JcePBESecretKeyEncryptorBuilder;

/**
 * A simple utility class that generates a RSA PGPPublicKey/PGPSecretKey pair.
 * <p>
 * usage: RSAKeyPairGenerator [-a] identity passPhrase
 * <p>
 * Where identity is the name to be associated with the public key. The keys are placed 
 * in the files pub.[asc|bpg] and secret.[asc|bpg].
 */
public class RSAKeyPairGenerator
{
    private static void exportKeyPair(
        OutputStream    secretOut,
        OutputStream    publicOut,
        KeyPair         pair,
        String          identity,
        char[]          passPhrase,
        boolean         armor)
        throws IOException, InvalidKeyException, NoSuchProviderException, SignatureException, PGPException
    {    
        if (armor)
        {
            secretOut = new ArmoredOutputStream(secretOut);
        }

        PGPDigestCalculator sha1Calc = new JcaPGPDigestCalculatorProviderBuilder().build().get(HashAlgorithmTags.SHA1);
        PGPKeyPair          keyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_GENERAL, pair, new Date());
        PGPSecretKey        secretKey = new PGPSecretKey(PGPSignature.DEFAULT_CERTIFICATION, keyPair, identity, sha1Calc, null, null, new JcaPGPContentSignerBuilder(keyPair.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1), new JcePBESecretKeyEncryptorBuilder(PGPEncryptedData.CAST5, sha1Calc).setProvider("SC").build(passPhrase));
        
        secretKey.encode(secretOut);
        
        secretOut.close();
        
        if (armor)
        {
            publicOut = new ArmoredOutputStream(publicOut);
        }

        PGPPublicKey    key = secretKey.getPublicKey();
        
        key.encode(publicOut);
        
        publicOut.close();
    }
    
    public static void main(
        String[] args)
        throws Exception
    {
        Security.addProvider(new BouncyCastleProvider());

        KeyPairGenerator    kpg = KeyPairGenerator.getInstance("RSA", "SC");
        
        kpg.initialize(1024);
        
        KeyPair                    kp = kpg.generateKeyPair();
        
        if (args.length < 2)
        {
            System.out.println("RSAKeyPairGenerator [-a] identity passPhrase");
            System.exit(0);
        }
        
        if (args[0].equals("-a"))
        {
            if (args.length < 3)
            {
                System.out.println("RSAKeyPairGenerator [-a] identity passPhrase");
                System.exit(0);
            }
            
            FileOutputStream    out1 = new FileOutputStream("secret.asc");
            FileOutputStream    out2 = new FileOutputStream("pub.asc");
            
            exportKeyPair(out1, out2, kp, args[1], args[2].toCharArray(), true);
        }
        else
        {
            FileOutputStream    out1 = new FileOutputStream("secret.bpg");
            FileOutputStream    out2 = new FileOutputStream("pub.bpg");
            
            exportKeyPair(out1, out2, kp, args[0], args[1].toCharArray(), false);
        }
    }
}
