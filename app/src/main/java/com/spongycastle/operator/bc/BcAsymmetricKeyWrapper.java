package com.spongycastle.operator.bc;

import java.security.SecureRandom;

import com.spongycastle.asn1.ASN1ObjectIdentifier;
import com.spongycastle.asn1.x509.AlgorithmIdentifier;
import com.spongycastle.crypto.AsymmetricBlockCipher;
import com.spongycastle.crypto.CipherParameters;
import com.spongycastle.crypto.InvalidCipherTextException;
import com.spongycastle.crypto.params.AsymmetricKeyParameter;
import com.spongycastle.crypto.params.ParametersWithRandom;
import com.spongycastle.operator.AsymmetricKeyWrapper;
import com.spongycastle.operator.GenericKey;
import com.spongycastle.operator.OperatorException;

public abstract class BcAsymmetricKeyWrapper
    extends AsymmetricKeyWrapper
{
    private AsymmetricKeyParameter publicKey;
    private SecureRandom random;

    public BcAsymmetricKeyWrapper(AlgorithmIdentifier encAlgId, AsymmetricKeyParameter publicKey)
    {
        super(encAlgId);

        this.publicKey = publicKey;
    }

    public BcAsymmetricKeyWrapper setSecureRandom(SecureRandom random)
    {
        this.random = random;

        return this;
    }

    public byte[] generateWrappedKey(GenericKey encryptionKey)
        throws OperatorException
    {
        AsymmetricBlockCipher keyEncryptionCipher = createAsymmetricWrapper(getAlgorithmIdentifier().getAlgorithm());
        
        CipherParameters params = publicKey;
        if (random != null)
        {
            params = new ParametersWithRandom(params, random);
        }

        try
        {
            byte[] keyEnc = OperatorUtils.getKeyBytes(encryptionKey);
            keyEncryptionCipher.init(true, params);
            return keyEncryptionCipher.processBlock(keyEnc, 0, keyEnc.length);
        }
        catch (InvalidCipherTextException e)
        {
            throw new OperatorException("unable to encrypt contents key", e);
        }
    }

    protected abstract AsymmetricBlockCipher createAsymmetricWrapper(ASN1ObjectIdentifier algorithm);
}
