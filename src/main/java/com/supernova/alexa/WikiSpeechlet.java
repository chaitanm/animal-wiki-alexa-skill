package com.supernova.alexa;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by chaitanya.m on 1/3/17.
 */
public class WikiSpeechlet implements Speechlet
{
    private static final Logger log = LoggerFactory.getLogger(WikiSpeechlet.class);

    private IWikiService wikiService;

    private ApplicationContext appContext;


    public WikiSpeechlet()
    {
        appContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        wikiService = (IWikiService) appContext.getBean("wikiService");
    }

    public void onSessionStarted(SessionStartedRequest sessionStartedRequest, Session session) throws SpeechletException
    {
        log.info("Session Started requestId = {}, sessionId = {}", sessionStartedRequest.getRequestId(), session.getSessionId());
    }

    public SpeechletResponse onLaunch(LaunchRequest launchRequest, Session session) throws SpeechletException
    {
        log.info("Launched requestId= {}, sessionId = {}", launchRequest.getRequestId(), session.getSessionId());
        return wikiService.getWelcomeResponse();
    }

    public SpeechletResponse onIntent(IntentRequest intentRequest, Session session) throws SpeechletException
    {
        log.info("Intent requestId = {}, sessionId = {}", intentRequest.getRequestId(), session.getSessionId());
        Intent intent = intentRequest.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;
        switch (intentName)
        {
            case "WikiIntent":
                return wikiService.getWikiResponse(intent.getSlot("keyword").getValue());
            case "AMAZON.HelpIntent":
                return wikiService.getHelpResponse();
            default:
                throw new SpeechletException("Invalid Intent");
        }
    }

    public void onSessionEnded(SessionEndedRequest sessionEndedRequest, Session session) throws SpeechletException
    {
        log.info("Session Ended requestId = {}, sessionId = {}", sessionEndedRequest.getRequestId(), session.getSessionId());
    }
}
