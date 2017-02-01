package com.spongycastle.cert.path.validations;

import java.math.BigInteger;

import com.spongycastle.asn1.x509.BasicConstraints;
import com.spongycastle.asn1.x509.Extension;
import com.spongycastle.cert.X509CertificateHolder;
import com.spongycastle.cert.path.CertPathValidation;
import com.spongycastle.cert.path.CertPathValidationContext;
import com.spongycastle.cert.path.CertPathValidationException;
import com.spongycastle.util.Memoable;

public class BasicConstraintsValidation
    implements CertPathValidation
{
    private boolean          isMandatory;
    private BasicConstraints bc;
    private int              maxPathLength;

    public BasicConstraintsValidation()
    {
        this(true);
    }

    public BasicConstraintsValidation(boolean isMandatory)
    {
        this.isMandatory = isMandatory;
    }

    public void validate(CertPathValidationContext context, X509CertificateHolder certificate)
        throws CertPathValidationException
    {
        if (maxPathLength < 0)
        {
            throw new CertPathValidationException("BasicConstraints path length exceeded");
        }

        context.addHandledExtension(Extension.basicConstraints);

        BasicConstraints certBC = BasicConstraints.fromExtensions(certificate.getExtensions());

        if (certBC != null)
        {
            if (bc != null)
            {
                if (certBC.isCA())
                {
                    BigInteger pathLengthConstraint = certBC.getPathLenConstraint();

                    if (pathLengthConstraint != null)
                    {
                        int plc = pathLengthConstraint.intValue();

                        if (plc < maxPathLength)
                        {
                            maxPathLength = plc;
                            bc = certBC;
                        }
                    }
                }
            }
            else
            {
                bc = certBC;
                if (certBC.isCA())
                {
                    maxPathLength = certBC.getPathLenConstraint().intValue();
                }
            }
        }
        else
        {
            if (bc != null)
            {
                maxPathLength--;
            }
        }

        if (isMandatory && bc == null)
        {
            throw new CertPathValidationException("BasicConstraints not present in path");
        }
    }

    public Memoable copy()
    {
        BasicConstraintsValidation v = new BasicConstraintsValidation(isMandatory);

        v.bc = this.bc;
        v.maxPathLength = this.maxPathLength;

        return v;
    }

    public void reset(Memoable other)
    {
        BasicConstraintsValidation v = (BasicConstraintsValidation)other;

        this.isMandatory = v.isMandatory;
        this.bc = v.bc;
        this.maxPathLength = v.maxPathLength;
    }
}
