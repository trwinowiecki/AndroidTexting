package com.spongycastle.jcajce.provider.asymmetric.ec;

import java.security.spec.ECGenParameterSpec;

import com.spongycastle.asn1.ASN1ObjectIdentifier;
import com.spongycastle.asn1.x9.X9ECParameters;
import com.spongycastle.jcajce.provider.asymmetric.util.ECUtil;

class ECUtils
{
    static X9ECParameters getDomainParametersFromGenSpec(ECGenParameterSpec genSpec)
    {
        return getDomainParametersFromName(genSpec.getName());
    }

    static X9ECParameters getDomainParametersFromName(String curveName)
    {
        X9ECParameters domainParameters;
        try
        {
            if (curveName.charAt(0) >= '0' && curveName.charAt(0) <= '2')
            {
                ASN1ObjectIdentifier oidID = new ASN1ObjectIdentifier(curveName);
                domainParameters = ECUtil.getNamedCurveByOid(oidID);
            }
            else
            {
                if (curveName.indexOf(' ') > 0)
                {
                    curveName = curveName.substring(curveName.indexOf(' ') + 1);
                    domainParameters = ECUtil.getNamedCurveByName(curveName);
                }
                else
                {
                    domainParameters = ECUtil.getNamedCurveByName(curveName);
                }
            }
        }
        catch (IllegalArgumentException ex)
        {
            domainParameters = ECUtil.getNamedCurveByName(curveName);
        }
        return domainParameters;
    }
}
