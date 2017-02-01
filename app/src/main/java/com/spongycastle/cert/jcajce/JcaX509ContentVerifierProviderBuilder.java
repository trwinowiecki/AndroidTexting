package com.spongycastle.cert.jcajce;

import java.security.Provider;
import java.security.cert.CertificateException;

import com.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import com.spongycastle.cert.X509CertificateHolder;
import com.spongycastle.cert.X509ContentVerifierProviderBuilder;
import com.spongycastle.operator.ContentVerifierProvider;
import com.spongycastle.operator.OperatorCreationException;
import com.spongycastle.operator.jcajce.JcaContentVerifierProviderBuilder;

public class JcaX509ContentVerifierProviderBuilder
    implements X509ContentVerifierProviderBuilder
{
    private JcaContentVerifierProviderBuilder builder = new JcaContentVerifierProviderBuilder();

    public JcaX509ContentVerifierProviderBuilder setProvider(Provider provider)
    {
        this.builder.setProvider(provider);

        return this;
    }

    public JcaX509ContentVerifierProviderBuilder setProvider(String providerName)
    {
        this.builder.setProvider(providerName);

        return this;
    }

    public ContentVerifierProvider build(SubjectPublicKeyInfo validatingKeyInfo)
        throws OperatorCreationException
    {
        return builder.build(validatingKeyInfo);
    }

    public ContentVerifierProvider build(X509CertificateHolder validatingKeyInfo)
        throws OperatorCreationException
    {
        try
        {
            return builder.build(validatingKeyInfo);
        }
        catch (CertificateException e)
        {
            throw new OperatorCreationException("Unable to process certificate: " + e.getMessage(), e);
        }
    }
}
