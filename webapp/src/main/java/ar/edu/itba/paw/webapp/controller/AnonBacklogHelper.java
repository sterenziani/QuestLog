package ar.edu.itba.paw.webapp.controller;
import java.util.ArrayList;
import java.util.List;
import ar.edu.itba.paw.webapp.dto.GameDto;

public class AnonBacklogHelper {
	public static void updateList(List<GameDto> list, String backlog) {
		List<Long> backlogList = getGamesInBacklog(backlog);
		for(GameDto g : list) {
			if(backlogList.contains(g.getId())) {
				g.setIn_backlog(true);
			}
		}
	}
	
	public static void updateDto(GameDto g, String backlog) {
		List<Long> backlogList = getGamesInBacklog(backlog);
		if(backlogList.contains(g.getId())) {
			g.setIn_backlog(true);
		}
	}
	
    private static List<Long> getGamesInBacklog(String backlog)
    {
        List<Long> list = new ArrayList<Long>();
        String[] ids = backlog.split("-");
        for(String id : ids)
        {
            if(!id.isEmpty())
                list.add(Long.parseLong(id));
        }
        return list;
    }
}
