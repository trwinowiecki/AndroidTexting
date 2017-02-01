package com.spongycastle.crypto.generators;

import java.math.BigInteger;

import com.spongycastle.crypto.AsymmetricCipherKeyPair;
import com.spongycastle.crypto.AsymmetricCipherKeyPairGenerator;
import com.spongycastle.crypto.KeyGenerationParameters;
import com.spongycastle.crypto.params.DHParameters;
import com.spongycastle.crypto.params.ElGamalKeyGenerationParameters;
import com.spongycastle.crypto.params.ElGamalParameters;
import com.spongycastle.crypto.params.ElGamalPrivateKeyParameters;
import com.spongycastle.crypto.params.ElGamalPublicKeyParameters;

/**
 * a ElGamal key pair generator.
 * <p>
 * This generates keys consistent for use with ElGamal as described in
 * page 164 of "Handbook of Applied Cryptography".
 */
public class ElGamalKeyPairGenerator
    implements AsymmetricCipherKeyPairGenerator
{
    private ElGamalKeyGenerationParameters param;

    public void init(
        KeyGenerationParameters param)
    {
        this.param = (ElGamalKeyGenerationParameters)param;
    }

    public AsymmetricCipherKeyPair generateKeyPair()
    {
        DHKeyGeneratorHelper helper = DHKeyGeneratorHelper.INSTANCE;
        ElGamalParameters egp = param.getParameters();
        DHParameters dhp = new DHParameters(egp.getP(), egp.getG(), null, egp.getL());  

        BigInteger x = helper.calculatePrivate(dhp, param.getRandom()); 
        BigInteger y = helper.calculatePublic(dhp, x);

        return new AsymmetricCipherKeyPair(
            new ElGamalPublicKeyParameters(y, egp),
            new ElGamalPrivateKeyParameters(x, egp));
    }
}
