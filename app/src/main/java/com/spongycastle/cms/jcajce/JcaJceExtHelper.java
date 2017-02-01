package com.spongycastle.cms.jcajce;

import java.security.PrivateKey;

import javax.crypto.SecretKey;

import com.spongycastle.asn1.x509.AlgorithmIdentifier;
import com.spongycastle.jcajce.util.JcaJceHelper;
import com.spongycastle.operator.SymmetricKeyUnwrapper;
import com.spongycastle.operator.jcajce.JceAsymmetricKeyUnwrapper;
import com.spongycastle.operator.jcajce.JceKTSKeyUnwrapper;

public interface JcaJceExtHelper
    extends JcaJceHelper
{
    JceAsymmetricKeyUnwrapper createAsymmetricUnwrapper(AlgorithmIdentifier keyEncryptionAlgorithm, PrivateKey keyEncryptionKey);

    JceKTSKeyUnwrapper createAsymmetricUnwrapper(AlgorithmIdentifier keyEncryptionAlgorithm, PrivateKey keyEncryptionKey, byte[] partyUInfo, byte[] partyVInfo);

    SymmetricKeyUnwrapper createSymmetricUnwrapper(AlgorithmIdentifier keyEncryptionAlgorithm, SecretKey keyEncryptionKey);
}
