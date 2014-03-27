package com.redmine.ui;

public interface ILoginView {
	public void setServerURL(String server);
	public void setName(String name);
	public void setPassword(String password);
	
	public String getServerURL();
	public String getName();
	public String getPassword();
	
	public void moveToTicketListView();
	public void showErrDlg(String title, String msg);
}
