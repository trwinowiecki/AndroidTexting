package com.spongycastle.cms.jcajce;

import java.security.cert.X509CertSelector;

import com.spongycastle.cms.KeyTransRecipientId;
import com.spongycastle.cms.SignerId;

public class JcaX509CertSelectorConverter
    extends com.spongycastle.cert.selector.jcajce.JcaX509CertSelectorConverter
{
    public JcaX509CertSelectorConverter()
    {
    }

    public X509CertSelector getCertSelector(KeyTransRecipientId recipientId)
    {
        return doConversion(recipientId.getIssuer(), recipientId.getSerialNumber(), recipientId.getSubjectKeyIdentifier());
    }

    public X509CertSelector getCertSelector(SignerId signerId)
    {
        return doConversion(signerId.getIssuer(), signerId.getSerialNumber(), signerId.getSubjectKeyIdentifier());
    }
}
