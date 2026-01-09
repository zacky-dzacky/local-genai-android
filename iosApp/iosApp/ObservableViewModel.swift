import Foundation
import shared

class ObservableViewModel<T: AnyObject>: ObservableObject {
    private let viewModel: T

    init(viewModel: T) {
        self.viewModel = viewModel
    }
}