package com.supernova.alexa;

import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazonaws.util.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chaitanya.m on 1/4/17.
 */
public class WikiService implements IWikiService
{
    private static final Logger log = LoggerFactory.getLogger(WikiService.class);

    public SpeechletResponse getWelcomeResponse()
    {
        String speechText = "Welcome to Alexa Animal Wiki. You can say any animal name to get it's details.";

        //Card Content
        SimpleCard card = new SimpleCard();
        card.setTitle("Welcome to Alexa Wiki!");
        card.setContent(speechText);

        //Speech Content
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        //Re-prompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    public SpeechletResponse getHelpResponse()
    {
        String speechText = "Alexa Animal Wiki will help you get the details of any animal. " +
                "You can say any animal name to me to get it's details.";

        //Card Content
        SimpleCard card = new SimpleCard();
        card.setTitle("Alexa Wiki - Help");
        card.setContent(speechText);

        //Speech Content
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        //Re-prompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);

    }

    public SpeechletResponse getWikiResponse(String keyword)
    {

        String data = getSummaryFromWiki(keyword);

        //Card Content
        SimpleCard card = new SimpleCard();
        card.setTitle("Alexa Animal Wiki - " + keyword);
        card.setContent(data);

        //Speech Content
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(data);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    private static String getSummaryFromWiki(String keyword)
    {
        String summary = null;
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost("en.wikipedia.org").setPath("/w/api.php")
                .setParameter("format", "json").setParameter("prop","extracts")
                .setParameter("action", "query").setParameter("exintro", "")
                .setParameter("explaintext", "").setParameter("exsentences","4")
                .setParameter("titles", keyword);
        String url = null;
        try
        {
            url = builder.build().toURL().toString();
        }
        catch (Exception e)
        {
            log.error("Url Construction Failed with Exception: {}", e.toString());
        }
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);
        try
        {
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK)
            {
                log.error("Method failed: " + method.getStatusLine());
            }
            byte[] responseBody = method.getResponseBody();
            String response = new String(responseBody);
            JSONObject obj = new JSONObject(response);
            JSONObject pages = obj.getJSONObject("query").getJSONObject("pages");
            summary = pages.getJSONObject(pages.keys().next().toString()).getString("extract");
        }
        catch (Exception e)
        {
            log.error("Fatal error while retrieving summary: " + e.getMessage());
        }
        finally
        {
            method.releaseConnection();
        }
        return summary;

    }
}
