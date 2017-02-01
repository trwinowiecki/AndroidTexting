package com.spongycastle.cms.jcajce;

import java.security.PrivateKey;

import javax.crypto.SecretKey;

import com.spongycastle.asn1.x509.AlgorithmIdentifier;
import com.spongycastle.jcajce.util.DefaultJcaJceHelper;
import com.spongycastle.operator.SymmetricKeyUnwrapper;
import com.spongycastle.operator.jcajce.JceAsymmetricKeyUnwrapper;
import com.spongycastle.operator.jcajce.JceKTSKeyUnwrapper;
import com.spongycastle.operator.jcajce.JceSymmetricKeyUnwrapper;

class DefaultJcaJceExtHelper
    extends DefaultJcaJceHelper
    implements JcaJceExtHelper
{
    public JceAsymmetricKeyUnwrapper createAsymmetricUnwrapper(AlgorithmIdentifier keyEncryptionAlgorithm, PrivateKey keyEncryptionKey)
    {
        return new JceAsymmetricKeyUnwrapper(keyEncryptionAlgorithm, keyEncryptionKey);
    }

    public JceKTSKeyUnwrapper createAsymmetricUnwrapper(AlgorithmIdentifier keyEncryptionAlgorithm, PrivateKey keyEncryptionKey, byte[] partyUInfo, byte[] partyVInfo)
    {
        return new JceKTSKeyUnwrapper(keyEncryptionAlgorithm, keyEncryptionKey, partyUInfo, partyVInfo);
    }

    public SymmetricKeyUnwrapper createSymmetricUnwrapper(AlgorithmIdentifier keyEncryptionAlgorithm, SecretKey keyEncryptionKey)
    {
        return new JceSymmetricKeyUnwrapper(keyEncryptionAlgorithm, keyEncryptionKey);
    }
}
