import java.util.*;

public class MeetingScheduler {
    static class Meeting {
        String startTime;
        int duration;
        List<String> participants;

        Meeting(String startTime, int duration, List<String> participants) {
            this.startTime = startTime;
            this.duration = duration;
            this.participants = participants;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Map<Integer, List<Meeting>> schedule = new HashMap<>();

        for (int i = 0; i < n; i++) {
            String[] input = scanner.nextLine().split(" ");
            if (input[0].equals("APPOINT")) {
                int day = Integer.parseInt(input[1]);
                String startTime = input[2];
                int duration = Integer.parseInt(input[3]);
                int k = Integer.parseInt(input[4]);
                List<String> participants = new ArrayList<>();
                for (int j = 0; j < k; j++) {
                    participants.add(input[5 + j]);
                }

                if (isAvailable(schedule, day, startTime, duration, participants)) {
                    scheduleMeeting(schedule, day, startTime, duration, participants);
                    System.out.println("OK");
                } else {
                    System.out.println("FAIL");
                    List<String> conflictingParticipants = findConflictingParticipants(schedule, day, startTime, duration);
                    for (String participant : conflictingParticipants) {
                        System.out.print(participant + " ");
                    }
                    System.out.println();
                }
            } else if (input[0].equals("PRINT")) {
                int day = Integer.parseInt(input[1]);
                String name = input[2];
                printMeetings(schedule, day, name);
            }
        }
    }

    static boolean isAvailable(Map<Integer, List<Meeting>> schedule, int day, String startTime, int duration, List<String> participants) {
        if (!schedule.containsKey(day)) {
            return true;
        }

        for (Meeting meeting : schedule.get(day)) {
            if (meeting.startTime.equals(startTime)) {
                return false;
            }

            int meetingStartMinutes = convertToMinutes(meeting.startTime);
            int meetingEndMinutes = meetingStartMinutes + meeting.duration;
            int requestedStartMinutes = convertToMinutes(startTime);
            int requestedEndMinutes = requestedStartMinutes + duration;

            if ((requestedStartMinutes >= meetingStartMinutes && requestedStartMinutes < meetingEndMinutes) ||
                    (requestedEndMinutes > meetingStartMinutes && requestedEndMinutes <= meetingEndMinutes)) {
                for (String participant : participants) {
                    if (meeting.participants.contains(participant)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    static int convertToMinutes(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

    static void scheduleMeeting(Map<Integer, List<Meeting>> schedule, int day, String startTime, int duration, List<String> participants) {
        if (!schedule.containsKey(day)) {
            schedule.put(day, new ArrayList<>());
        }
        schedule.get(day).add(new Meeting(startTime, duration, participants));
    }

    static List<String> findConflictingParticipants(Map<Integer, List<Meeting>> schedule, int day, String startTime, int duration) {
        List<String> conflictingParticipants = new ArrayList<>();
        if (schedule.containsKey(day)) {
            for (Meeting meeting : schedule.get(day)) {
                int meetingStartMinutes = convertToMinutes(meeting.startTime);
                int meetingEndMinutes = meetingStartMinutes + meeting.duration;
                int requestedStartMinutes = convertToMinutes(startTime);
                int requestedEndMinutes = requestedStartMinutes + duration;

                if ((requestedStartMinutes >= meetingStartMinutes && requestedStartMinutes < meetingEndMinutes) ||
                        (requestedEndMinutes > meetingStartMinutes && requestedEndMinutes <= meetingEndMinutes)) {
                    conflictingParticipants.addAll(meeting.participants);
                }
            }
        }
        return conflictingParticipants;
    }

    static void printMeetings(Map<Integer, List<Meeting>> schedule, int day, String name) {
        if (!schedule.containsKey(day)) {
            return;
        }

        for (Meeting meeting : schedule.get(day)) {
            if (meeting.participants.contains(name)) {
                System.out.println(meeting.startTime + " " + meeting.duration + " " + String.join(" ", meeting.participants));
            }
        }
    }
}
