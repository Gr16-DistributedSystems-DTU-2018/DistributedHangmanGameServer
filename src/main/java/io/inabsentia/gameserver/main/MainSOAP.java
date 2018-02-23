package io.inabsentia.gameserver.main;

import io.inabsentia.gameserver.logic.soap.GameLogic;
import io.inabsentia.gameserver.logic.soap.IGameLogic;
import io.inabsentia.gameserver.util.Utils;

import javax.xml.ws.Endpoint;

public final class MainSOAP {

    public static void main(String[] args) throws Exception {
        IGameLogic logic = new GameLogic();

        Endpoint.publish(Utils.SOAP_STUB_URL_REMOTE_LOGIC_JAVABOG, logic);

        System.out.println("GameLogic (SOAP) registered at: " + Utils.SOAP_STUB_URL_REMOTE_LOGIC_JAVABOG);
    }

}