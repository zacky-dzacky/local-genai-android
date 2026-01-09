import SwiftUI
import shared

struct ContentView: View {
    @ObservedObject private var viewModel: ObservableViewModel<SharedViewModel>

    init() {
        let koin = KoinKt.doInitKoin()
        self.viewModel = ObservableViewModel(viewModel: koin.get(for: SharedViewModel.self) as! SharedViewModel)
    }

    var body: some View {
        let uiState = viewModel.uiState

        VStack {
            if uiState.processing {
                ProgressView()
            }

            Text(uiState.generatedText)
                .padding()

            Button(action: {
                self.viewModel.processUserPrompt(userPrompt: "Tell me a joke")
            }) {
                Text("Generate")
            }
        }
    }
}