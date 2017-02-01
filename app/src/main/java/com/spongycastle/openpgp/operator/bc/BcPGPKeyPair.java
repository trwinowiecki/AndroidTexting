package com.spongycastle.openpgp.operator.bc;

import java.util.Date;

import com.spongycastle.crypto.AsymmetricCipherKeyPair;
import com.spongycastle.crypto.params.AsymmetricKeyParameter;
import com.spongycastle.openpgp.PGPAlgorithmParameters;
import com.spongycastle.openpgp.PGPException;
import com.spongycastle.openpgp.PGPKeyPair;
import com.spongycastle.openpgp.PGPPrivateKey;
import com.spongycastle.openpgp.PGPPublicKey;

public class BcPGPKeyPair
    extends PGPKeyPair
{
    private static PGPPublicKey getPublicKey(int algorithm, PGPAlgorithmParameters parameters, AsymmetricKeyParameter pubKey, Date date)
        throws PGPException
    {
        return new BcPGPKeyConverter().getPGPPublicKey(algorithm, parameters, pubKey, date);
    }

    private static PGPPrivateKey getPrivateKey(PGPPublicKey pub, AsymmetricKeyParameter privKey)
        throws PGPException
    {
        return new BcPGPKeyConverter().getPGPPrivateKey(pub, privKey);
    }

    public BcPGPKeyPair(int algorithm, AsymmetricCipherKeyPair keyPair, Date date)
        throws PGPException
    {
        this.pub = getPublicKey(algorithm, null, keyPair.getPublic(), date);
        this.priv = getPrivateKey(this.pub, keyPair.getPrivate());
    }

    public BcPGPKeyPair(int algorithm, PGPAlgorithmParameters parameters, AsymmetricCipherKeyPair keyPair, Date date)
        throws PGPException
    {
        this.pub = getPublicKey(algorithm, parameters, keyPair.getPublic(), date);
        this.priv = getPrivateKey(this.pub, keyPair.getPrivate());
    }
}
