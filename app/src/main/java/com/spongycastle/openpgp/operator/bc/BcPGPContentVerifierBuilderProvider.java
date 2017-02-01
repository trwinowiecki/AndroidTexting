package com.spongycastle.openpgp.operator.bc;

import java.io.OutputStream;

import com.spongycastle.crypto.Signer;
import com.spongycastle.openpgp.PGPException;
import com.spongycastle.openpgp.PGPPublicKey;
import com.spongycastle.openpgp.operator.PGPContentVerifier;
import com.spongycastle.openpgp.operator.PGPContentVerifierBuilder;
import com.spongycastle.openpgp.operator.PGPContentVerifierBuilderProvider;

public class BcPGPContentVerifierBuilderProvider
    implements PGPContentVerifierBuilderProvider
{
    private BcPGPKeyConverter keyConverter = new BcPGPKeyConverter();

    public BcPGPContentVerifierBuilderProvider()
    {
    }

    public PGPContentVerifierBuilder get(int keyAlgorithm, int hashAlgorithm)
        throws PGPException
    {
        return new BcPGPContentVerifierBuilder(keyAlgorithm, hashAlgorithm);
    }

    private class BcPGPContentVerifierBuilder
        implements PGPContentVerifierBuilder
    {
        private int hashAlgorithm;
        private int keyAlgorithm;

        public BcPGPContentVerifierBuilder(int keyAlgorithm, int hashAlgorithm)
        {
            this.keyAlgorithm = keyAlgorithm;
            this.hashAlgorithm = hashAlgorithm;
        }

        public PGPContentVerifier build(final PGPPublicKey publicKey)
            throws PGPException
        {
            final Signer signer = BcImplProvider.createSigner(keyAlgorithm, hashAlgorithm);

            signer.init(false, keyConverter.getPublicKey(publicKey));

            return new PGPContentVerifier()
            {
                public int getHashAlgorithm()
                {
                    return hashAlgorithm;
                }

                public int getKeyAlgorithm()
                {
                    return keyAlgorithm;
                }

                public long getKeyID()
                {
                    return publicKey.getKeyID();
                }

                public boolean verify(byte[] expected)
                {
                    return signer.verifySignature(expected);
                }

                public OutputStream getOutputStream()
                {
                    return new SignerOutputStream(signer);
                }
            };
        }
    }
}
