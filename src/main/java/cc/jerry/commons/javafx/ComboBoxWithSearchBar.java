/*
 * Copyright 2018 Siyuan "Jerry" Zhang.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.jerry.commons.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ComboBoxWithSearchBar<T> extends ComboBox<T> implements EventHandler<KeyEvent> {

    private ObservableList<T> data;
    private boolean moveCaretToPos = false;
    private int caretPos;

    public ComboBoxWithSearchBar() {
    	setEditable(true); 
        data = getItems();
        
        setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent t) {
            	hide();
            }
        });
        this.setOnKeyReleased(ComboBoxWithSearchBar.this);
    }
    
    public void updateItems() {
    	data = getItems(); 
    }

    @Override
    public void handle(KeyEvent event) {
    	
        if (event.getCode() == KeyCode.UP) {
            caretPos = -1;
            moveCaret(getEditor().getText().length());
            return;
        } else if (event.getCode() == KeyCode.DOWN) {
            if (!isShowing()) {
                show();
            }
            caretPos = -1;
            moveCaret(getEditor().getText().length());
            return;
        } else if (event.getCode() == KeyCode.BACK_SPACE) {
        	hide(); 
            moveCaretToPos = true;
            caretPos = getEditor().getCaretPosition();
        } else if (event.getCode() == KeyCode.DELETE) {
        	hide(); 
            moveCaretToPos = true;
            caretPos = getEditor().getCaretPosition();
        } else if (event.getCode() == KeyCode.ENTER) {
        	if (this.getSelectionModel().getSelectedIndex() != -1)
        		hide(); 
        	else {
        		this.getSelectionModel().select(0); 
        		hide(); 
        	}
        	return; 
        }

        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
                || event.isControlDown() || event.getCode() == KeyCode.HOME
                || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
            return;
        }
        
        ObservableList<T> list = FXCollections.observableArrayList();
        ObservableList<T> containList = FXCollections.observableArrayList(); 
        ObservableList<T> splitList = FXCollections.observableArrayList(); 
        ObservableList<T> remList = FXCollections.observableArrayList(); 
        for (int i = 0; i < data.size(); i++) {
            if(data.get(i).toString().toLowerCase().startsWith(
              ComboBoxWithSearchBar.this.getEditor().getText().toLowerCase())) {
                list.add(data.get(i));
                continue; 
            }
            else if (data.get(i).toString().toLowerCase().contains(
              ComboBoxWithSearchBar.this.getEditor().getText().toLowerCase())) {
            	containList.add(data.get(i)); 
            	continue; 
            }
            else if (data.get(i).toString().toLowerCase().contains(" ")) {
            	boolean proceed = true; 
            	
            	for (String subString : ComboBoxWithSearchBar.this.getEditor().getText().toLowerCase().split(" ")) {
            		if (!data.get(i).toString().toLowerCase().contains(subString)) {
            			proceed = false; 
            			break; 
            		}
            	}
            	
            	if (proceed) {
            		splitList.add(data.get(i)); 
                	continue; 
            	}
            }
            remList.add(data.get(i)); 
        }
        
        String t = getEditor().getText(); 
        
        setItems(list);
        getItems().addAll(containList); 
        getItems().addAll(splitList); 
        getItems().addAll(remList); 
        getEditor().setText(t);
        if(!moveCaretToPos) {
            caretPos = -1;
        }
        moveCaret(t.length());
        if (!list.isEmpty()) {
            show();
        }
        else hide(); 
    }

    private void moveCaret(int textLength) {
        if(caretPos == -1) {
            getEditor().positionCaret(textLength);
        } else {
            getEditor().positionCaret(caretPos);
        }
        moveCaretToPos = false;
    }
    
    /**
     * This method gives the name of the absolute selected item, not the text that is inputed by the user in the editor. 
     * 
     * @return	returns the <b>string</b> that is associated with the item selected by the user, or <b>null</b> if nothing is selected. 
     */
    public String getSelectedItem() {
    	try {
    		return this.getItems().get(this.getSelectionModel().getSelectedIndex()).toString(); 
    	} catch (ArrayIndexOutOfBoundsException e) {
    		return null; 
    	}
    }

}