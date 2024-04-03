package com.example.translationapp

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var SLanguageEd:EditText
    private lateinit var TLanguageTv:TextView
    private lateinit var CLaguageBtn:MaterialButton
    private lateinit var targetChooseBtn:MaterialButton
    private lateinit var translateBtn:MaterialButton

    companion object{
        private  const val TAG="MAIN_TAG"
    }

    //will contain list with language
    private var languageArrayList:ArrayList<ModelLanguage>?=null

    //default /select language
    private var sourceLanguageCode="en"
    private var sourceLanguageTitle="English"
    private var targetLanguageCode="ur"
    private var targetLanguageTitle="Urdu"

    private lateinit var translatorOption:TranslatorOptions
    private lateinit var translator:Translator
    private lateinit var  progressDialog:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SLanguageEd=findViewById(R.id.sourceLanguageEt)
        TLanguageTv=findViewById(R.id.targetLanguageTv)
        CLaguageBtn=findViewById(R.id.sourceLanguageChooseBtn)
        targetChooseBtn=findViewById(R.id.targetLanguageChooseBtn)
        translateBtn=findViewById(R.id.translateBtn)

        // init Dialog
        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)
        LoadAvailableLanguage()

        CLaguageBtn.setOnClickListener {
            sourceLanguageChoose()

        }
        targetChooseBtn.setOnClickListener {
            targetLanguageChoose()
        }
        translateBtn.setOnClickListener {
            vaildateDate()
        }



    }

    private var sourceLanguageText=""
    private fun vaildateDate() {
        sourceLanguageText=SLanguageEd.text.toString().trim()
        Log.d(TAG, "vaildateDate: sourceLanguageText$sourceLanguageText")
        if (sourceLanguageText.isEmpty()){
            showToast("Enter text to translate....")
        }
        else{
            startTranslations()
        }
    }

    private fun startTranslations() {
        progressDialog.setMessage("Processing Language Model....")
        progressDialog.show()

        translatorOption = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguageCode)
            .setTargetLanguage(targetLanguageCode).build()
        translator = Translation.getClient(translatorOption)

        val downloadCondition = DownloadConditions.Builder()
            .requireWifi()
            .build()
        translator.downloadModelIfNeeded(downloadCondition)
            .addOnSuccessListener {
                Log.d(TAG, "startTranslations: model read, start translation ...")

                progressDialog.setMessage("Translation........")
                translator.translate(sourceLanguageText)
                    .addOnSuccessListener {TranslateText ->
                        Log.d(TAG, "startTranslations: TranslateText$TranslateText")
                        progressDialog.dismiss()
                        TLanguageTv.text=TranslateText
                    }
                    .addOnFailureListener { e->
                        progressDialog.dismiss()
                        Log.d(TAG, "startTranslations: ",e)
                        showToast("failed to translate due to ${e.message}")
                    }
            }
            .addOnFailureListener {e->
                progressDialog.dismiss()
                Log.d(TAG, "startTranslations: ",e)
                showToast("failed to translate due to ${e.message}")
            }
    }
    private fun LoadAvailableLanguage() {
      languageArrayList= ArrayList()
        val languageCodeList=TranslateLanguage.getAllLanguages()
        for (languageCode in languageCodeList){
            val  languageTitle=Locale(languageCode).displayLanguage
            Log.d(TAG, "LoadAvailableLanguage: languageCode:$languageCode")
            Log.d(TAG, "LoadAvailableLanguage: languageTitle:$languageTitle")

            val modelLanguage=ModelLanguage(languageCode,languageTitle)
            languageArrayList!!.add(modelLanguage)
        }
    }
    private fun sourceLanguageChoose(){
        val popMenu=PopupMenu(this,CLaguageBtn)
        for (i in languageArrayList!!.indices){
            popMenu.menu.add(Menu.NONE,i,i, languageArrayList!![i].languageTitle)
        }
        popMenu.show()

        popMenu.setOnMenuItemClickListener {menuItem ->
            val position =menuItem.itemId
            sourceLanguageCode=languageArrayList!![position].languageCode
            sourceLanguageTitle=languageArrayList!![position].languageTitle
            CLaguageBtn.text=sourceLanguageTitle
            SLanguageEd.hint="Enter$sourceLanguageTitle"
            Log.d(TAG, "sourceLanguageChoose: sourceLanguageCode$sourceLanguageCode")
            Log.d(TAG, "sourceLanguageChoose: sourceLanguageTitle$sourceLanguageTitle")
            false
        }
    }
    private fun targetLanguageChoose() {
        val popMenu = PopupMenu(this, targetChooseBtn)
        for (i in languageArrayList!!.indices) {
            popMenu.menu.add(Menu.NONE, i, i, languageArrayList!![i].languageTitle)
        }
        popMenu.show()
        popMenu.setOnMenuItemClickListener {menuItem->
            val position=menuItem.itemId
            targetLanguageCode=languageArrayList!![position].languageCode
            targetLanguageTitle=languageArrayList!![position].languageTitle
            targetChooseBtn.text=targetLanguageTitle
            Log.d(TAG, "targetLanguageChoose: targetLanguageCode$targetLanguageCode")
            Log.d(TAG, "targetLanguageChoose: targetLanguageTitle$targetLanguageTitle")


            false
        }
    }
    private fun showToast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()


    }
}