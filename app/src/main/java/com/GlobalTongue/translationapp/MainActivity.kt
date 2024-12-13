package com.GlobalTongue.translationapp

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.GlobalTongue.translationapp.MainActivity

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import java.util.Locale
//androidx.appcompat.widget.AppCompatButton
class MainActivity : AppCompatActivity() {
    var translationResult: String = ""
    lateinit var SLanguageEd:EditText
    lateinit var TLanguageTv:TextView
    lateinit var CLaguageBtn:AppCompatButton
    lateinit var baseline_arrow_forward_24:ImageView
    lateinit var targetChooseBtn:AppCompatButton
    lateinit var translateBtn:AppCompatButton

    companion object{
        private  const val TAG="MAIN_TAG"
    }

    //will contain list with language
    var languageArrayList:ArrayList<ModelLanguage>?=null

    //default /select language
    var sourceLanguageCode="en"
    var sourceLanguageTitle="English"
    var targetLanguageCode="ur"
    var targetLanguageTitle="Urdu"

    private lateinit var translatorOption:TranslatorOptions
    lateinit var translator:Translator
    lateinit var  progressDialog:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.navigationBarColor =Color.BLACK // تغيير لون شريط التنقل السفلي
            window.statusBarColor = Color.BLACK // تغيير لون شريط الحالة

        }
        SLanguageEd=findViewById(R.id.sourceLanguageEt)
        TLanguageTv=findViewById(R.id.targetLanguageTv)
        CLaguageBtn=findViewById(R.id.sourceLanguageChooseBtn)
        targetChooseBtn=findViewById(R.id.targetLanguageChooseBtn)
        translateBtn=findViewById(R.id.translateBtn)
        baseline_arrow_forward_24=findViewById(R.id.baseAarray)

        // init Dialog
        progressDialog= ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)
        LoadAvailableLanguage()

        CLaguageBtn.setOnClickListener {
            sourceLanguageChoose()
            translate()

        }
        targetChooseBtn.setOnClickListener {
            targetLanguageChoose()
            translate()
        }
        translateBtn.setOnClickListener {
            vaildateDate()
            translate()
        }



    }

    var sourceLanguageText=""
    fun translate() {
        // Assume translation logic here and displaying result in targetLanguageTv
        // After displaying the translation, hide the Translate button


        // Check if translation result is visible
        if (translationResult.isNotEmpty()) {
            TLanguageTv.text = translationResult
            // Set alpha value to make buttons more transparent
            CLaguageBtn.alpha = 0.5f // Half transparent
            targetChooseBtn.alpha = 0.5f // Half transparent
            translateBtn.alpha = 0.5f
            baseline_arrow_forward_24.alpha = 0.5f

        } else {
            // If the result is empty, make buttons even more transparent
            CLaguageBtn.alpha = 0.2f // More transparent
            targetChooseBtn.alpha = 0.2f // More transparent
            translateBtn.alpha = 0.2f
            baseline_arrow_forward_24.alpha = 0.2f
        }
    }
    fun vaildateDate() {
        sourceLanguageText=SLanguageEd.text.toString().trim()
        Log.d(TAG, "vaildateDate: sourceLanguageText$sourceLanguageText")
        if (sourceLanguageText.isEmpty()){
            showToast("Enter text to translate....")
        }
        else{
            startTranslations()
        }
    }

    fun startTranslations() {
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
    fun sourceLanguageChoose(){
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
    fun targetLanguageChoose() {
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
    fun showToast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()


    }
}