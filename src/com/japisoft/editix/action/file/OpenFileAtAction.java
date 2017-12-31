package com.japisoft.editix.action.file;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URLDecoder;

import javax.swing.AbstractAction;

import com.japisoft.framework.application.descriptor.ActionModel;
import com.japisoft.editix.document.DocumentModel;
import com.japisoft.editix.ui.EditixFrame;
import com.japisoft.editix.ui.EditixFactory;
import com.japisoft.xmlpad.XMLContainer;
import com.japisoft.xmlpad.editor.XMLPadDocument;

/**
This program is available under two licenses : 

1. For non commercial usage : 

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

2. For commercial usage :

You need to get a commercial license for source usage at : 

http://www.editix.com/buy.html

Copyright (c) 2018 Alexandre Brillant - JAPISOFT SARL - http://www.japisoft.com

@author Alexandre Brillant - abrillant@japisoft.com
@author JAPISOFT SARL - http://www.japisoft.com

*/
public class OpenFileAtAction extends AbstractAction {

	public void actionPerformed( ActionEvent e ) {
		XMLContainer container = EditixFrame.THIS.getSelectedContainer();
		if ( container == null )
			return;
		XMLPadDocument doc = container.getXMLDocument();
		String word = doc.getWordAt(
			container.getEditor().getCaretPosition() );
		
		if ( word == null ) {
			EditixFactory.buildAndShowErrorDialog( "No file found" );
			return;
		}

		String fileName = null;

		if ( word.startsWith( "file://" ) ) {
			fileName = URLDecoder.decode( word.substring( 7 ) );
		} else
			if ( word.startsWith( "file:" ) ) {
				fileName = URLDecoder.decode( word.substring( 5 ) );
			} else {
				String loc = container.getCurrentDocumentLocation();
				if ( loc == null ) {
					fileName = word;
				} else {
					File f = new File( new File( loc ).getParentFile(), word );
					if ( f.exists() ) {
						fileName = f.toString();
					} else
						fileName = word;
				}
			}
		
		String type = DocumentModel.getDocumentByFileName( fileName ).getType();

		if ( fileName.indexOf( "://" ) == -1 ) {
			if (!OpenAction.openFile( type, true, new File( fileName ), null )) {
				EditixFactory.buildAndShowErrorDialog("Can't open " + fileName );
			}
		}
		else
			ActionModel.activeActionById( ActionModel.OPEN, e, fileName, type );
	}

}