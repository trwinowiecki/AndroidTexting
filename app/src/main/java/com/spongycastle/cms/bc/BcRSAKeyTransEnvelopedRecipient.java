package com.spongycastle.cms.bc;

import java.io.InputStream;

import com.spongycastle.asn1.x509.AlgorithmIdentifier;
import com.spongycastle.cms.CMSException;
import com.spongycastle.cms.RecipientOperator;
import com.spongycastle.crypto.BufferedBlockCipher;
import com.spongycastle.crypto.CipherParameters;
import com.spongycastle.crypto.StreamCipher;
import com.spongycastle.crypto.io.CipherInputStream;
import com.spongycastle.crypto.params.AsymmetricKeyParameter;
import com.spongycastle.operator.InputDecryptor;

public class BcRSAKeyTransEnvelopedRecipient
    extends BcKeyTransRecipient
{
    public BcRSAKeyTransEnvelopedRecipient(AsymmetricKeyParameter key)
    {
        super(key);
    }

    public RecipientOperator getRecipientOperator(AlgorithmIdentifier keyEncryptionAlgorithm, final AlgorithmIdentifier contentEncryptionAlgorithm, byte[] encryptedContentEncryptionKey)
        throws CMSException
    {
        CipherParameters secretKey = extractSecretKey(keyEncryptionAlgorithm, contentEncryptionAlgorithm, encryptedContentEncryptionKey);

        final Object dataCipher = EnvelopedDataHelper.createContentCipher(false, secretKey, contentEncryptionAlgorithm);

        return new RecipientOperator(new InputDecryptor()
        {
            public AlgorithmIdentifier getAlgorithmIdentifier()
            {
                return contentEncryptionAlgorithm;
            }

            public InputStream getInputStream(InputStream dataIn)
            {
                if (dataCipher instanceof BufferedBlockCipher)
                {
                    return new CipherInputStream(dataIn, (BufferedBlockCipher)dataCipher);
                }
                else
                {
                    return new CipherInputStream(dataIn, (StreamCipher)dataCipher);
                }
            }
        });
    }
}
