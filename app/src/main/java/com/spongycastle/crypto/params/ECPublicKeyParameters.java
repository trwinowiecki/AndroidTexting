package com.spongycastle.crypto.params;

import com.spongycastle.math.ec.ECPoint;

public class ECPublicKeyParameters
    extends ECKeyParameters
{
    ECPoint Q;

    public ECPublicKeyParameters(
        ECPoint             Q,
        ECDomainParameters  params)
    {
        super(false, params);
        this.Q = Q.normalize();
    }

    public ECPoint getQ()
    {
        return Q;
    }
}
