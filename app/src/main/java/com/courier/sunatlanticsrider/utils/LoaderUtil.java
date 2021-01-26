package com.courier.sunatlanticsrider.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import com.courier.sunatlanticsrider.R;


public class LoaderUtil {

    String toBeUpdate="";

    public static Dialog showProgressBar(Context context) {

        Dialog csprogress;
        csprogress = new Dialog(context, R.style.MyAlertDialogStyle);
        csprogress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        csprogress.setCancelable(false);
        csprogress.setContentView(R.layout.layout_progressbar);
        csprogress.setCanceledOnTouchOutside(true);
        csprogress.show();
        return csprogress;
    }

    public static void dismisProgressBar(Context context, Dialog dialog) {

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

    }


    /*public String showAlertDialog(Activity context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = context.getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.layout_dialog_userprofile, null);

        builder.setView(view);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText getUserName = view.findViewById(R.id.updateName);
                //Toast.makeText(context.t,"Entered User Name"+getUserName.getText().toString(),Toast.LENGTH_LONG).show();

                toBeUpdate=getUserName.getText().toString();

                System.out.println("Entered UserName" + getUserName.getText().toString());


            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        return toBeUpdate;

    }*/


}
