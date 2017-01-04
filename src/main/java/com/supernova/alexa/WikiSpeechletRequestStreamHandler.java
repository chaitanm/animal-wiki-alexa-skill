package com.supernova.alexa;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by chaitanya.m on 1/3/17.
 */
public class WikiSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler
{

    private static final Set<String> supportedApplicationIds = new HashSet<String>();
    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        supportedApplicationIds.add("amzn1.ask.skill.8a65ebde-a8bd-4929-8e8a-5aaceb032f4e");
    }

    public WikiSpeechletRequestStreamHandler()
    {
        super(new WikiSpeechlet(), supportedApplicationIds);
    }
}
