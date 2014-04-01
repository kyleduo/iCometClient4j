package com.kyleduo.icomet;

public interface IConnCallback {

	// connect to iComet server unsuccessfully
	public void onFail(String msg);

	// connect to iComet server successfully
	public void onSuccess();

	// connection cut by server or there's an error
	public void onDisconnect();

	// connection stopped by user (as well as the client)
	public void onStop();
	
	/**
	 * called when the client need to reconnect to the server
	 * @param times show how many times this reconnection is
	 * @return boolean return true if you want to intercept the reconnection; false for that you want the client to try to reconnect to the iComet server
	 */
	public boolean onReconnect(int times);
	
	// reconnection success 
	public void onReconnectSuccess(int times);
}
