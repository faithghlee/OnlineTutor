package com.asanka.tutor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Online Tutor.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    public final Stripe stripe = new Stripe();

    public Stripe getStripe() {
        return stripe;
    }

    public static class Stripe {
        private String public_key;
        private String private_key;
        private int unitPrice;

        public String getPublic_key() {
            return public_key;
        }

        public void setPublic_key(String public_key) {
            this.public_key = public_key;
        }

        public String getPrivate_key() {
            return private_key;
        }

        public void setPrivate_key(String private_key) {
            this.private_key = private_key;
        }

        public int getUnitPrice() { return unitPrice; }

        public void setUnitPrice(int unitPrice) { this.unitPrice = unitPrice; }
    }
}
