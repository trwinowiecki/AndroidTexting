package com.spongycastle.openpgp.operator.jcajce;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

import com.spongycastle.openpgp.PGPAlgorithmParameters;
import com.spongycastle.openpgp.PGPException;
import com.spongycastle.openpgp.PGPKeyPair;
import com.spongycastle.openpgp.PGPPrivateKey;
import com.spongycastle.openpgp.PGPPublicKey;

public class JcaPGPKeyPair
    extends PGPKeyPair
{
    private static PGPPublicKey getPublicKey(int algorithm, PublicKey pubKey, Date date)
        throws PGPException
    {
        return  new JcaPGPKeyConverter().getPGPPublicKey(algorithm, pubKey, date);
    }

    private static PGPPublicKey getPublicKey(int algorithm, PGPAlgorithmParameters algorithmParameters, PublicKey pubKey, Date date)
        throws PGPException
    {
        return  new JcaPGPKeyConverter().getPGPPublicKey(algorithm, algorithmParameters, pubKey, date);
    }

    private static PGPPrivateKey getPrivateKey(PGPPublicKey pub, PrivateKey privKey)
        throws PGPException
    {
        return new JcaPGPKeyConverter().getPGPPrivateKey(pub, privKey);
    }

    public JcaPGPKeyPair(int algorithm, KeyPair keyPair, Date date)
        throws PGPException
    {
        this.pub = getPublicKey(algorithm, keyPair.getPublic(), date);
        this.priv = getPrivateKey(this.pub, keyPair.getPrivate());
    }

    public JcaPGPKeyPair(int algorithm, PGPAlgorithmParameters parameters, KeyPair keyPair, Date date)
        throws PGPException
    {
        this.pub = getPublicKey(algorithm, parameters, keyPair.getPublic(), date);
        this.priv = getPrivateKey(this.pub, keyPair.getPrivate());
    }
}
