package com.spongycastle.openpgp.operator.jcajce;

import java.security.Provider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.spongycastle.jcajce.util.DefaultJcaJceHelper;
import com.spongycastle.jcajce.util.NamedJcaJceHelper;
import com.spongycastle.jcajce.util.ProviderJcaJceHelper;
import com.spongycastle.openpgp.PGPException;
import com.spongycastle.openpgp.operator.PBEDataDecryptorFactory;
import com.spongycastle.openpgp.operator.PGPDataDecryptor;
import com.spongycastle.openpgp.operator.PGPDigestCalculatorProvider;

/**
 * Builder for {@link PBEDataDecryptorFactory} instances that obtain cryptographic primitives using
 * the JCE API.
 */
public class JcePBEDataDecryptorFactoryBuilder
{
    private OperatorHelper helper = new OperatorHelper(new DefaultJcaJceHelper());
    private PGPDigestCalculatorProvider calculatorProvider;

    /**
     * Base constructor.
     *
     * @param calculatorProvider   a digest calculator provider to provide calculators to support the key generation calculation required.
     */
    public JcePBEDataDecryptorFactoryBuilder(PGPDigestCalculatorProvider calculatorProvider)
    {
        this.calculatorProvider = calculatorProvider;
    }

    /**
     * Set the provider object to use for creating cryptographic primitives in the resulting factory the builder produces.
     *
     * @param provider  provider object for cryptographic primitives.
     * @return  the current builder.
     */
    public JcePBEDataDecryptorFactoryBuilder setProvider(Provider provider)
    {
        this.helper = new OperatorHelper(new ProviderJcaJceHelper(provider));

        return this;
    }

    /**
     * Set the provider name to use for creating cryptographic primitives in the resulting factory the builder produces.
     *
     * @param providerName  the name of the provider to reference for cryptographic primitives.
     * @return  the current builder.
     */
    public JcePBEDataDecryptorFactoryBuilder setProvider(String providerName)
    {
        this.helper = new OperatorHelper(new NamedJcaJceHelper(providerName));

        return this;
    }

    /**
     * Construct a {@link PBEDataDecryptorFactory} to use to decrypt PBE encrypted data.
     *
     * @param passPhrase the pass phrase to use to generate keys in the resulting factory.
     * @return a decryptor factory that can be used to generate PBE keys.
     */
    public PBEDataDecryptorFactory build(char[] passPhrase)
    {
         return new PBEDataDecryptorFactory(passPhrase, calculatorProvider)
         {
             public byte[] recoverSessionData(int keyAlgorithm, byte[] key, byte[] secKeyData)
                 throws PGPException
             {
                 try
                 {
                     if (secKeyData != null && secKeyData.length > 0)
                     {
                         String cipherName = PGPUtil.getSymmetricCipherName(keyAlgorithm);
                         Cipher keyCipher = helper.createCipher(cipherName + "/CFB/NoPadding");

                         keyCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, cipherName), new IvParameterSpec(new byte[keyCipher.getBlockSize()]));

                         return keyCipher.doFinal(secKeyData);
                     }
                     else
                     {
                         byte[] keyBytes = new byte[key.length + 1];

                         keyBytes[0] = (byte)keyAlgorithm;
                         System.arraycopy(key, 0, keyBytes, 1, key.length);

                         return keyBytes;
                     }
                 }
                 catch (Exception e)
                 {
                     throw new PGPException("Exception recovering session info", e);
                 }
             }

             public PGPDataDecryptor createDataDecryptor(boolean withIntegrityPacket, int encAlgorithm, byte[] key)
                 throws PGPException
             {
                 return helper.createDataDecryptor(withIntegrityPacket, encAlgorithm, key);
             }
         };
    }
}
