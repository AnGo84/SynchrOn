package com.synchron.google;

/**
 * Created by AnGo on 25.05.2017.
 */
public class GoogleAPIProject {
    private String userName;
    private String projectName;
    private String pathToJson;

    public GoogleAPIProject() {
    }

//    public GoogleAPIProject(String projectName, String pathToJson) {
//        this.projectName = projectName;
//        this.pathToJson = pathToJson;
//    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String name) {
        this.projectName = name;
    }

    public String getPathToJson() {
        return pathToJson;
    }

    public void setPathToJson(String pathToJson) {
        this.pathToJson = pathToJson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GoogleAPIProject)) return false;

        GoogleAPIProject that = (GoogleAPIProject) o;

        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (!projectName.equals(that.projectName)) return false;
        return pathToJson.equals(that.pathToJson);
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + projectName.hashCode();
        result = 31 * result + pathToJson.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GoogleAPIProject{");
        sb.append("userName='").append(userName).append('\'');
        sb.append(", projectName='").append(projectName).append('\'');
        sb.append(", pathToJson='").append(pathToJson).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
