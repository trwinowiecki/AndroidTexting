package com.spongycastle.pqc.jcajce.provider.mceliece;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import com.spongycastle.crypto.AsymmetricCipherKeyPair;
import com.spongycastle.pqc.crypto.mceliece.McElieceCCA2KeyGenerationParameters;
import com.spongycastle.pqc.crypto.mceliece.McElieceCCA2KeyPairGenerator;
import com.spongycastle.pqc.crypto.mceliece.McElieceCCA2Parameters;
import com.spongycastle.pqc.crypto.mceliece.McElieceCCA2PrivateKeyParameters;
import com.spongycastle.pqc.crypto.mceliece.McElieceCCA2PublicKeyParameters;
import com.spongycastle.pqc.crypto.mceliece.McElieceKeyGenerationParameters;
import com.spongycastle.pqc.crypto.mceliece.McElieceKeyPairGenerator;
import com.spongycastle.pqc.crypto.mceliece.McElieceParameters;
import com.spongycastle.pqc.crypto.mceliece.McEliecePrivateKeyParameters;
import com.spongycastle.pqc.crypto.mceliece.McEliecePublicKeyParameters;
import com.spongycastle.pqc.jcajce.spec.ECCKeyGenParameterSpec;
import com.spongycastle.pqc.jcajce.spec.McElieceCCA2ParameterSpec;

public abstract class McElieceKeyPairGeneratorSpi
    extends KeyPairGenerator
{
    public McElieceKeyPairGeneratorSpi(
        String algorithmName)
    {
        super(algorithmName);
    }

    /**
     *
     *
     *
     */

    public static class McElieceCCA2
        extends McElieceKeyPairGeneratorSpi
    {

        McElieceCCA2KeyPairGenerator kpg;


        public McElieceCCA2()
        {
            super("McElieceCCA-2");
        }

        public McElieceCCA2(String s)
        {
            super(s);
        }

        public void initialize(AlgorithmParameterSpec params)
            throws InvalidAlgorithmParameterException
        {
            kpg = new McElieceCCA2KeyPairGenerator();
            super.initialize(params);
            ECCKeyGenParameterSpec ecc = (ECCKeyGenParameterSpec)params;

            McElieceCCA2KeyGenerationParameters mccca2KGParams = new McElieceCCA2KeyGenerationParameters(new SecureRandom(), new McElieceCCA2Parameters(ecc.getM(), ecc.getT()));
            kpg.init(mccca2KGParams);
        }

        public void initialize(int keySize, SecureRandom random)
        {
            McElieceCCA2ParameterSpec paramSpec = new McElieceCCA2ParameterSpec();

            // call the initializer with the chosen parameters
            try
            {
                this.initialize(paramSpec);
            }
            catch (InvalidAlgorithmParameterException ae)
            {
            }
        }

        public KeyPair generateKeyPair()
        {
            AsymmetricCipherKeyPair generateKeyPair = kpg.generateKeyPair();
            McElieceCCA2PrivateKeyParameters sk = (McElieceCCA2PrivateKeyParameters)generateKeyPair.getPrivate();
            McElieceCCA2PublicKeyParameters pk = (McElieceCCA2PublicKeyParameters)generateKeyPair.getPublic();

            return new KeyPair(new BCMcElieceCCA2PublicKey(pk), new BCMcElieceCCA2PrivateKey(sk));

        }

    }

    /**
     *
     *
     *
     */

    public static class McEliece
        extends McElieceKeyPairGeneratorSpi
    {

        McElieceKeyPairGenerator kpg;


        public McEliece()
        {
            super("McEliece");
        }

        public void initialize(AlgorithmParameterSpec params)
            throws InvalidAlgorithmParameterException
        {
            kpg = new McElieceKeyPairGenerator();
            super.initialize(params);
            ECCKeyGenParameterSpec ecc = (ECCKeyGenParameterSpec)params;

            McElieceKeyGenerationParameters mccKGParams = new McElieceKeyGenerationParameters(new SecureRandom(), new McElieceParameters(ecc.getM(), ecc.getT()));
            kpg.init(mccKGParams);
        }

        public void initialize(int keySize, SecureRandom random)
        {
            ECCKeyGenParameterSpec paramSpec = new ECCKeyGenParameterSpec();

            // call the initializer with the chosen parameters
            try
            {
                this.initialize(paramSpec);
            }
            catch (InvalidAlgorithmParameterException ae)
            {
            }
        }

        public KeyPair generateKeyPair()
        {
            AsymmetricCipherKeyPair generateKeyPair = kpg.generateKeyPair();
            McEliecePrivateKeyParameters sk = (McEliecePrivateKeyParameters)generateKeyPair.getPrivate();
            McEliecePublicKeyParameters pk = (McEliecePublicKeyParameters)generateKeyPair.getPublic();

            return new KeyPair(new BCMcEliecePublicKey(pk), new BCMcEliecePrivateKey(sk));
        }

    }

}
