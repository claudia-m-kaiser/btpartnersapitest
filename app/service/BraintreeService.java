package service;

import com.braintreegateway.WebhookNotification;

import java.text.SimpleDateFormat;

/**
 * Created by clkaiser on 26/02/15.
 */
public class BraintreeService {

    private final BraintreeConfiguration configuration;

    public BraintreeService(BraintreeConfiguration configuration) {
        this.configuration = configuration;
    }

    ////////////////////////////////////////////Webhooks///////////////////////////////////////////


    public String getWebhookVerificationResponse(String btChallenge){
        return configuration.getGateway().webhookNotification().verify(btChallenge);

    }

    public String parseWebhookNotification(String bt_signature, String bt_payload){

        WebhookNotification webhookNotification = configuration.getGateway().webhookNotification().parse(bt_signature,bt_payload);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
        String date = dateFormat.format(webhookNotification.getTimestamp().getTime());

        String message;

        if(webhookNotification.getKind().equals("partner_merchant_connected")){
            message = "Merchant ID: " + webhookNotification.getPartnerMerchant().getMerchantPublicId()
                            + " **** Public Key: " + webhookNotification.getPartnerMerchant().getPublicKey()
                            + " **** Private Key: " + webhookNotification.getPartnerMerchant().getPrivateKey();

        }else {

            message = "Date: " + date + " **** Type: " + webhookNotification.getKind();
        }

        return message;
    }
}
