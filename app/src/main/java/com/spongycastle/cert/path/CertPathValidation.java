package com.spongycastle.cert.path;

import com.spongycastle.cert.X509CertificateHolder;
import com.spongycastle.util.Memoable;

public interface CertPathValidation
    extends Memoable
{
    public void validate(CertPathValidationContext context, X509CertificateHolder certificate)
        throws CertPathValidationException;
}
