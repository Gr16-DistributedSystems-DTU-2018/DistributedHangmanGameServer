package server.main;

import server.logic.soap.GameLogic;
import server.logic.soap.IGameLogic;
import server.util.Utils;

import javax.xml.ws.Endpoint;

public final class MainSOAP {

    public static void main(String[] args) throws Exception {
        IGameLogic logic = new GameLogic();

        Endpoint.publish(Utils.SOAP_STUB_URL_REMOTE_LOGIC_JAVABOG, logic);

        System.out.println("GameLogic (SOAP) registered at: " + Utils.SOAP_STUB_URL_REMOTE_LOGIC_JAVABOG);
    }

}