package org.tmatesoft.hg.test;

import java.io.File;
import java.io.IOException;

import org.tmatesoft.hg.core.HgCatCommand;
import org.tmatesoft.hg.core.HgDataStreamException;
import org.tmatesoft.hg.core.HgException;
import org.tmatesoft.hg.core.HgRepoFacade;
import org.tmatesoft.hg.core.HgStatus;
import org.tmatesoft.hg.core.HgStatusCommand;
import org.tmatesoft.hg.internal.ByteArrayChannel;
import org.tmatesoft.hg.util.ByteChannel;
import org.tmatesoft.hg.util.CancelledException;

public class TestHgStatusCommand  extends HgRepoFacade{
	public void run() {
		String repositoryPath = "g:/localRepo/strategy";
		try {
			if (!initFrom(new File(repositoryPath))) {
				System.out.printf("No Mercurial repository found at '%s'.\n",getRepository().getLocation());
				System.exit(1);
			}
			HgStatusCommand statusCommand = createStatusCommand();
			
			System.out.printf("About to display working tree status (%s)\n\n",getRepository().getLocation());

			statusCommand.revision(9);
			
			statusCommand.all().execute(new HgStatusCommand.Handler() {
				public void handleStatus(HgStatus status) {
						HgCatCommand catCommand = createCatCommand();
						HgStatus.Kind kind = status.getKind();
						if(!HgStatus.Kind.Clean.equals(kind) && !HgStatus.Kind.Ignored.equals(kind)){							
							System.out.printf("%s %s\n", status.getKind(), status.getPath());
							catCommand.file(status.getPath());
							catCommand.file(status.getPath());
							catCommand.revision(10);
							ByteChannel sink = new ByteArrayChannel();
							try {
								catCommand.execute(sink);
							} catch (HgDataStreamException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (CancelledException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
				}
			});
			System.out.printf("\nStatus of the working tree is displayed above.");
		} catch (HgException e) {
			System.out.printf("Error accessing Mercurial repository at '%s'.\n", getRepository().getLocation());
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static void main(String[] args){
		TestHgStatusCommand command = new TestHgStatusCommand();
		command.run();
	}
	
}
