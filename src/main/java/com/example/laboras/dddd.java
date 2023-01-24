package com.example.laboras;

public void editInfo(View view) {

    EditText login = findViewById(R.id.newUser);
    EditText newPassword = findViewById(R.id.newPass);
    EditText confirmPassword = findViewById(R.id.confirmPass);

    if(newPassword.getText().toString().equals(confirmPassword.getText().toString()))
    {
        String data = "{\"userLogin\":\"" + login.getText().toString() + "\", \"userPassword\": \"" + newPassword.getText().toString() + "\"}";

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RESTController.sendPut(USER_UPDATE_URL, data);
                handler.post(() ->{
                    System.out.println("Response: "+response);
                    try {
                        if (!response.equals("")  && !response.equals("No such user")) {
                            Gson gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            Type updatedUser = new TypeToken<User>() {
                            }.getType();
                            final User updatedUserJSON = gsonBuilder.fromJson(response, updatedUser);
                            Intent intent = new Intent(EditInfo.this, MyCoursesActivity.class);
                            intent.putExtra("userInfo", response);
                            intent.putExtra("userLogin", updatedUserJSON.getLogin());
                            startActivity(intent);
                        } else {
                            Toast.makeText(EditInfo.this, "Errors while authenticating", Toast.LENGTH_LONG).show();
                        }
                        } catch (Exception e) {
                        e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                });
            } else
            {
                Toast.makeText(EditInfo.this, "Your passwords do not match.", Toast.LENGTH_LONG).show();
            }

}

