package com.spongycastle.cert.crmf.jcajce;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

import com.spongycastle.asn1.crmf.EncryptedValue;
import com.spongycastle.cert.crmf.CRMFException;
import com.spongycastle.cert.crmf.EncryptedValueBuilder;
import com.spongycastle.cert.jcajce.JcaX509CertificateHolder;
import com.spongycastle.operator.KeyWrapper;
import com.spongycastle.operator.OutputEncryptor;

public class JcaEncryptedValueBuilder
    extends EncryptedValueBuilder
{
    public JcaEncryptedValueBuilder(KeyWrapper wrapper, OutputEncryptor encryptor)
    {
        super(wrapper, encryptor);
    }

    public EncryptedValue build(X509Certificate certificate)
        throws CertificateEncodingException, CRMFException
    {
        return build(new JcaX509CertificateHolder(certificate));
    }
}
