package com.spongycastle.operator.bc;

import com.spongycastle.asn1.ASN1ObjectIdentifier;
import com.spongycastle.asn1.pkcs.PKCSObjectIdentifiers;
import com.spongycastle.asn1.x509.AlgorithmIdentifier;
import com.spongycastle.crypto.AsymmetricBlockCipher;
import com.spongycastle.crypto.InvalidCipherTextException;
import com.spongycastle.crypto.params.AsymmetricKeyParameter;
import com.spongycastle.operator.AsymmetricKeyUnwrapper;
import com.spongycastle.operator.GenericKey;
import com.spongycastle.operator.OperatorException;

public abstract class BcAsymmetricKeyUnwrapper
    extends AsymmetricKeyUnwrapper
{
    private AsymmetricKeyParameter privateKey;

    public BcAsymmetricKeyUnwrapper(AlgorithmIdentifier encAlgId, AsymmetricKeyParameter privateKey)
    {
        super(encAlgId);

        this.privateKey = privateKey;
    }

    public GenericKey generateUnwrappedKey(AlgorithmIdentifier encryptedKeyAlgorithm, byte[] encryptedKey)
        throws OperatorException
    {
        AsymmetricBlockCipher keyCipher = createAsymmetricUnwrapper(this.getAlgorithmIdentifier().getAlgorithm());

        keyCipher.init(false, privateKey);
        try
        {
            byte[] key = keyCipher.processBlock(encryptedKey, 0, encryptedKey.length);

            if (encryptedKeyAlgorithm.getAlgorithm().equals(PKCSObjectIdentifiers.des_EDE3_CBC))
            {
                return new GenericKey(encryptedKeyAlgorithm, key);
            }
            else
            {
                return new GenericKey(encryptedKeyAlgorithm, key);
            }
        }
        catch (InvalidCipherTextException e)
        {
            throw new OperatorException("unable to recover secret key: " + e.getMessage(), e);
        }
    }

    protected abstract AsymmetricBlockCipher createAsymmetricUnwrapper(ASN1ObjectIdentifier algorithm);
}
