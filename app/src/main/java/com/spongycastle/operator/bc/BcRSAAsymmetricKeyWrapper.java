package com.spongycastle.operator.bc;

import java.io.IOException;

import com.spongycastle.asn1.ASN1ObjectIdentifier;
import com.spongycastle.asn1.x509.AlgorithmIdentifier;
import com.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import com.spongycastle.crypto.AsymmetricBlockCipher;
import com.spongycastle.crypto.encodings.PKCS1Encoding;
import com.spongycastle.crypto.engines.RSAEngine;
import com.spongycastle.crypto.params.AsymmetricKeyParameter;
import com.spongycastle.crypto.util.PublicKeyFactory;

public class BcRSAAsymmetricKeyWrapper
    extends BcAsymmetricKeyWrapper
{
    public BcRSAAsymmetricKeyWrapper(AlgorithmIdentifier encAlgId, AsymmetricKeyParameter publicKey)
    {
        super(encAlgId, publicKey);
    }

    public BcRSAAsymmetricKeyWrapper(AlgorithmIdentifier encAlgId, SubjectPublicKeyInfo publicKeyInfo)
        throws IOException
    {
        super(encAlgId, PublicKeyFactory.createKey(publicKeyInfo));
    }

    protected AsymmetricBlockCipher createAsymmetricWrapper(ASN1ObjectIdentifier algorithm)
    {
        return new PKCS1Encoding(new RSAEngine());
    }
}
