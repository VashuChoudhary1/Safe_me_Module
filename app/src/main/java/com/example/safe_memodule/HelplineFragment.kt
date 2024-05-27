package com.example.safe_memodule

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import android.Manifest
import androidx.core.content.ContextCompat


class HelplineFragment : Fragment() {

    private val REQUEST_CALL_PHONE = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_helpline, container, false)
    }
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val callPoliceButton: Button = view.findViewById(R.id.call_police_button)
    val callWomenHelplineButton: Button = view.findViewById(R.id.call_women_helpline_button)
    val calllAmbulanceButton: Button = view.findViewById(R.id.call_ambulance_button)
    val callCyberCrimeHelplineButton:Button =  view.findViewById(R.id.call_Cyber_helpline_button)

    callPoliceButton.setOnClickListener {
        makePhoneCall("112")
    }

    callWomenHelplineButton.setOnClickListener {
        makePhoneCall("181")
    }
    calllAmbulanceButton.setOnClickListener{
        makePhoneCall("108")
    }
    callCyberCrimeHelplineButton.setOnClickListener{
        makePhoneCall("1930")
    }
}
    private fun makePhoneCall(phoneNumber: String) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), REQUEST_CALL_PHONE)
        } else {
            startCall(phoneNumber)
        }
    }
    private fun startCall(phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$phoneNumber")
        startActivity(callIntent)
    }
override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == REQUEST_CALL_PHONE) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }
}
}

