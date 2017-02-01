package com.spongycastle.openssl.jcajce;

import java.security.PrivateKey;

import com.spongycastle.asn1.pkcs.PrivateKeyInfo;
import com.spongycastle.openssl.PKCS8Generator;
import com.spongycastle.operator.OutputEncryptor;
import com.spongycastle.util.io.pem.PemGenerationException;

public class JcaPKCS8Generator
    extends PKCS8Generator
{
    public JcaPKCS8Generator(PrivateKey key, OutputEncryptor encryptor)
         throws PemGenerationException
    {
         super(PrivateKeyInfo.getInstance(key.getEncoded()), encryptor);
    }
}
