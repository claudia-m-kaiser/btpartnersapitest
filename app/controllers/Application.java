package controllers;

import play.*;
import play.mvc.*;

import service.BraintreeConfiguration;
import service.BraintreeConfiguration.BraintreeEnvironment;
import service.BraintreeService;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Application extends Controller {

    protected static BraintreeService sandboxBtService = new BraintreeService(new BraintreeConfiguration(BraintreeEnvironment.Sandbox));
    protected static BraintreeService productionBtService = new BraintreeService(new BraintreeConfiguration(BraintreeEnvironment.Production));

    protected static BraintreeService currService = sandboxBtService;

    public static Result index() {
        return ok();
    }

    public static Result initialSetup(String bt_challenge){

        if(bt_challenge.isEmpty()){
            Logger.error("Could not get bt challenge");
        }else{
            Logger.info("BT Challenge is: " + bt_challenge);
        }

        String verification = currService.getWebhookVerificationResponse(bt_challenge);

        return ok(verification);
    }

    public static Result listener(){

        final Map<String, String[]> webhook = request().body().asFormUrlEncoded();
        String bt_signature = webhook.get("bt_signature")[0].toString();
        String bt_payload = webhook.get("bt_payload")[0].toString();

        String parsedWebhook = currService.parseWebhookNotification(bt_signature,bt_payload);

        System.out.println(parsedWebhook);

        return ok();
    }

    public static Result cancel(){
        return ok("Partner merchant cancelled");
    }

    public static Result error(){
        return ok("Partner merchant error occured");
    }

    public static Result redirect(){
        return ok("Partner merchant redirected");
    }

}
