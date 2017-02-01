package com.spongycastle.openssl.jcajce;

import java.security.Provider;

import com.spongycastle.jcajce.util.DefaultJcaJceHelper;
import com.spongycastle.jcajce.util.JcaJceHelper;
import com.spongycastle.jcajce.util.NamedJcaJceHelper;
import com.spongycastle.jcajce.util.ProviderJcaJceHelper;
import com.spongycastle.openssl.PEMDecryptor;
import com.spongycastle.openssl.PEMDecryptorProvider;
import com.spongycastle.openssl.PEMException;
import com.spongycastle.openssl.PasswordException;

public class JcePEMDecryptorProviderBuilder
{
    private JcaJceHelper helper = new DefaultJcaJceHelper();

    public JcePEMDecryptorProviderBuilder setProvider(Provider provider)
    {
        this.helper = new ProviderJcaJceHelper(provider);

        return this;
    }

    public JcePEMDecryptorProviderBuilder setProvider(String providerName)
    {
        this.helper = new NamedJcaJceHelper(providerName);

        return this;
    }

    public PEMDecryptorProvider build(final char[] password)
    {
        return new PEMDecryptorProvider()
        {
            public PEMDecryptor get(final String dekAlgName)
            {
                return new PEMDecryptor()
                {
                    public byte[] decrypt(byte[] keyBytes, byte[] iv)
                        throws PEMException
                    {
                        if (password == null)
                        {
                            throw new PasswordException("Password is null, but a password is required");
                        }

                        return PEMUtilities.crypt(false, helper, keyBytes, password, dekAlgName, iv);
                    }
                };
            }
        };
    }
}
