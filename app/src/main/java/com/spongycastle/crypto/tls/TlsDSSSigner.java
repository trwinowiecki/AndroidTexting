package com.spongycastle.crypto.tls;

import com.spongycastle.crypto.DSA;
import com.spongycastle.crypto.params.AsymmetricKeyParameter;
import com.spongycastle.crypto.params.DSAPublicKeyParameters;
import com.spongycastle.crypto.signers.DSASigner;
import com.spongycastle.crypto.signers.HMacDSAKCalculator;

public class TlsDSSSigner
    extends TlsDSASigner
{
    public boolean isValidPublicKey(AsymmetricKeyParameter publicKey)
    {
        return publicKey instanceof DSAPublicKeyParameters;
    }

    protected DSA createDSAImpl(short hashAlgorithm)
    {
        return new DSASigner(new HMacDSAKCalculator(TlsUtils.createHash(hashAlgorithm)));
    }

    protected short getSignatureAlgorithm()
    {
        return SignatureAlgorithm.dsa;
    }
}
