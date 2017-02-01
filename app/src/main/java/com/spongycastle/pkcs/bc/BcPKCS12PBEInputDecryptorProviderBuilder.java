package com.spongycastle.pkcs.bc;

import java.io.InputStream;

import com.spongycastle.asn1.pkcs.PKCS12PBEParams;
import com.spongycastle.asn1.x509.AlgorithmIdentifier;
import com.spongycastle.crypto.CipherParameters;
import com.spongycastle.crypto.ExtendedDigest;
import com.spongycastle.crypto.digests.SHA1Digest;
import com.spongycastle.crypto.generators.PKCS12ParametersGenerator;
import com.spongycastle.crypto.io.CipherInputStream;
import com.spongycastle.crypto.paddings.PaddedBufferedBlockCipher;
import com.spongycastle.operator.GenericKey;
import com.spongycastle.operator.InputDecryptor;
import com.spongycastle.operator.InputDecryptorProvider;

public class BcPKCS12PBEInputDecryptorProviderBuilder
{
    private ExtendedDigest digest;

    public BcPKCS12PBEInputDecryptorProviderBuilder()
    {
         this(new SHA1Digest());
    }

    public BcPKCS12PBEInputDecryptorProviderBuilder(ExtendedDigest digest)
    {
         this.digest = digest;
    }

    public InputDecryptorProvider build(final char[] password)
    {
        return new InputDecryptorProvider()
        {
            public InputDecryptor get(final AlgorithmIdentifier algorithmIdentifier)
            {
                final PaddedBufferedBlockCipher engine = PKCS12PBEUtils.getEngine(algorithmIdentifier.getAlgorithm());

                PKCS12PBEParams           pbeParams = PKCS12PBEParams.getInstance(algorithmIdentifier.getParameters());

                CipherParameters params = PKCS12PBEUtils.createCipherParameters(algorithmIdentifier.getAlgorithm(), digest, engine.getBlockSize(), pbeParams, password);

                engine.init(false, params);

                return new InputDecryptor()
                {
                    public AlgorithmIdentifier getAlgorithmIdentifier()
                    {
                        return algorithmIdentifier;
                    }

                    public InputStream getInputStream(InputStream input)
                    {
                        return new CipherInputStream(input, engine);
                    }

                    public GenericKey getKey()
                    {
                        return new GenericKey(PKCS12ParametersGenerator.PKCS12PasswordToBytes(password));
                    }
                };
            }
        };

    }
}
