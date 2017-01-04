package com.supernova.alexa;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletResponse;

/**
 * Created by chaitanya.m on 1/4/17.
 */
public interface IWikiService
{
    SpeechletResponse getWelcomeResponse();

    SpeechletResponse getHelpResponse();

    SpeechletResponse getWikiResponse(String keyword);

}
