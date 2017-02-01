package com.spongycastle.operator.bc;

import com.spongycastle.asn1.kisa.KISAObjectIdentifiers;
import com.spongycastle.asn1.x509.AlgorithmIdentifier;

class SEEDUtil
{
    static AlgorithmIdentifier determineKeyEncAlg()
    {
        // parameters absent
        return new AlgorithmIdentifier(
            KISAObjectIdentifiers.id_npki_app_cmsSeed_wrap);
    }
}
