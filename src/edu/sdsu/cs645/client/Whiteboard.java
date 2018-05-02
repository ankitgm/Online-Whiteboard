package edu.sdsu.cs645.client;


import com.google.gwt.core.client.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Whiteboard implements EntryPoint {
  /**
   * The message displayed to the user when the server cannot be reached or
   * returns an error.
   */
  private static final String SERVER_ERROR = "An error occurred while "
      + "attempting to contact the server. Please check your network "
      + "connection and try again.";

  /**
   * Create a remote service proxy to talk to the server-side Greeting service.
   */
  private final WhiteboardServiceAsync whiteboardService = GWT.create(WhiteboardService.class);


private HTML status;
//private PasswordTextBox password;
private RichTextArea board;

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
        status = new HTML();
        status.getElement().setId("status_msg");  
        buildLogin();
    }
    private void buildLogin() {

        FlowPanel loginPanel = new FlowPanel();
        // loginPanel.setStyleName("log_panel");
        loginPanel.getElement().setId("log_panel");
        final PasswordTextBox password = new PasswordTextBox();
        loginPanel.add(new HTML("<h1> Please enter your password </h1>"));
        loginPanel.add(new Label("Password"));
        loginPanel.add(password);
        FlowPanel bPanel = new FlowPanel();
        bPanel.setStyleName("blog_panel");
        Button loginButton = new Button ("Login");
        Button clearButton = new Button ("Clear");
        loginButton.setStyleName("log_button");
        clearButton.setStyleName("log_button");
        clearButton.addClickHandler(new  ClickHandler () {
          public void onClick(ClickEvent e)
          {
            status.setText("");
            password.setText("");
            password.setFocus(true);
          }
        });
      
          loginButton.addClickHandler(new  ClickHandler () {
          public void onClick(ClickEvent e)
          {
            validateLogin(password.getText());
            password.setFocus(true);
          }
        });

        bPanel.add(clearButton);
        bPanel.add(loginButton);
        bPanel.add(new HTML("<br /> Code by Ankit Goutam Mohanty, jadrn023"));
        loginPanel.add(bPanel);
        loginPanel.add(status);
        RootPanel.get().add(loginPanel);
        password.setFocus(true);
    }

private void validateLogin(String login) {
  AsyncCallback callback = new AsyncCallback() {
    public void onSuccess(Object results) {
      String answer  = (String) results;
      if (answer.equals("OK")) {
        status.setText("");
        buildMainPanel();
      }
      else
        status.setText("ERROR, invalid password");
      }
      public void onFailure(Throwable err) 
      {
        status.setText("Failed" + err.getMessage());
        err.printStackTrace();
      }
    };
    whiteboardService.validateLogin(login, callback);
  }
  private void buildMainPanel() {
    FlowPanel main = new FlowPanel();
    main.add(new HTML("<h1>Online Whiteboard</h1>"));
    main.add(getButtonPanel());
    board = new RichTextArea();
    main.add(board);
    main.add(status);
    main.add(new HTML("<h3>Code by Ankit Goutam Mohanty, jadrn023</h3>"));
    RootPanel.get().clear();
    RootPanel.get().add(main);
    board.setFocus(true);
    loadPanel();
  }
  private FlowPanel getButtonPanel() {
    FlowPanel p = new FlowPanel();
    Button clr = new Button ("Clear");
    Button save = new Button ("Save");
    Button load = new Button("Load");
    clr.setStyleName("my-button");
    save.setStyleName("my-button");
    load.setStyleName("my-button");



    clr.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent e) {
        board.setHTML("");
        status.setHTML("");
      }
    });
 save.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent e) {
       savePanel();
      }
    });
 load.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent e) {
        loadPanel();
      }
    });
p.setStyleName("button-panel");
p.add(clr);
p.add(load);
p.add(save);
return p; 
    
  }



  private void savePanel() {
  AsyncCallback callback = new AsyncCallback() {
    public void onSuccess(Object results) {
      String answer  = (String) results;
      if (answer.equals("OK")) {
        status.setText("Whiteboard saved");
        
      }
      else
        status.setText("ERROR, could not save content");
      }
      public void onFailure(Throwable err) 
      {
        status.setText("Failed" + err.getMessage());
        err.printStackTrace();
      }
    };
    whiteboardService.save(board.getHTML(), callback);
  }


    private void loadPanel() {
  AsyncCallback callback = new AsyncCallback() {
    public void onSuccess(Object results) {
      String answer  = (String) results;
      board.setHTML(answer);
        status.setText("Whiteboard loaded from disk!");
      }
      public void onFailure(Throwable err) 
      {
        status.setText("Failed" + err.getMessage());
        err.printStackTrace();
      }
    };
    whiteboardService.load(callback);
  }


}


  
