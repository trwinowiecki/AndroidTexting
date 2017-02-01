package com.spongycastle.cert.crmf.jcajce;

import java.security.PrivateKey;

import javax.security.auth.x500.X500Principal;

import com.spongycastle.asn1.pkcs.PrivateKeyInfo;
import com.spongycastle.asn1.x500.X500Name;
import com.spongycastle.asn1.x509.GeneralName;
import com.spongycastle.cert.crmf.PKIArchiveControlBuilder;

public class JcaPKIArchiveControlBuilder
    extends PKIArchiveControlBuilder
{
    public JcaPKIArchiveControlBuilder(PrivateKey privateKey, X500Name name)
    {
        this(privateKey, new GeneralName(name));
    }

    public JcaPKIArchiveControlBuilder(PrivateKey privateKey, X500Principal name)
    {
        this(privateKey, X500Name.getInstance(name.getEncoded()));
    }

    public JcaPKIArchiveControlBuilder(PrivateKey privateKey, GeneralName generalName)
    {
        super(PrivateKeyInfo.getInstance(privateKey.getEncoded()), generalName);
    }
}
