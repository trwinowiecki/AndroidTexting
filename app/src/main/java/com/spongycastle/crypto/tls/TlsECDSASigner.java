package com.spongycastle.crypto.tls;

import com.spongycastle.crypto.DSA;
import com.spongycastle.crypto.params.AsymmetricKeyParameter;
import com.spongycastle.crypto.params.ECPublicKeyParameters;
import com.spongycastle.crypto.signers.ECDSASigner;
import com.spongycastle.crypto.signers.HMacDSAKCalculator;

public class TlsECDSASigner
    extends TlsDSASigner
{
    public boolean isValidPublicKey(AsymmetricKeyParameter publicKey)
    {
        return publicKey instanceof ECPublicKeyParameters;
    }

    protected DSA createDSAImpl(short hashAlgorithm)
    {
        return new ECDSASigner(new HMacDSAKCalculator(TlsUtils.createHash(hashAlgorithm)));
    }

    protected short getSignatureAlgorithm()
    {
        return SignatureAlgorithm.ecdsa;
    }
}
