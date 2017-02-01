package com.spongycastle.operator.bc;

import com.spongycastle.asn1.ASN1ObjectIdentifier;
import com.spongycastle.asn1.x509.AlgorithmIdentifier;
import com.spongycastle.crypto.AsymmetricBlockCipher;
import com.spongycastle.crypto.encodings.PKCS1Encoding;
import com.spongycastle.crypto.engines.RSAEngine;
import com.spongycastle.crypto.params.AsymmetricKeyParameter;

public class BcRSAAsymmetricKeyUnwrapper
    extends BcAsymmetricKeyUnwrapper
{
    public BcRSAAsymmetricKeyUnwrapper(AlgorithmIdentifier encAlgId, AsymmetricKeyParameter privateKey)
    {
        super(encAlgId, privateKey);
    }

    protected AsymmetricBlockCipher createAsymmetricUnwrapper(ASN1ObjectIdentifier algorithm)
    {
        return new PKCS1Encoding(new RSAEngine());
    }
}
