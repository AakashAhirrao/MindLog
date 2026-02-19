
    public enum Category {
        WORK("Work"),
        PERSONAL("Personal"),
        IDEA("Idea"),
        FITNESS("Fitness"),
        IMPORTANT("Important");

        private final String prettyName;

        Category(String prettyName){
            this.prettyName = prettyName;
        }

        public String getPrettyName(){
            return prettyName;
        }
    }

